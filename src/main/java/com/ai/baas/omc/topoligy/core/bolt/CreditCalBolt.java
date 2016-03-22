package com.ai.baas.omc.topoligy.core.bolt;
import java.util.List;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.ai.baas.omc.topoligy.core.business.OmcCalProcessor;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.dto.Dto4CreditCal;
import com.ai.baas.omc.topoligy.core.dto.Dto4CreditNotice;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.pojo.RealTimeBalance;
import com.ai.baas.omc.topoligy.core.pojo.SectionRule;
import com.ai.baas.omc.topoligy.core.util.CacheClient;
import com.ai.baas.omc.topoligy.core.util.OmcUtils;
import com.ai.baas.omc.topoligy.core.util.UrlClient;
import com.ai.baas.omc.topoligy.core.util.db.JdbcParam;
import com.ai.baas.omc.topoligy.core.util.db.JdbcProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Values;


/**
 * 信用度计算
 * 
 */
public class CreditCalBolt extends BaseBasicBolt {
	private static final long serialVersionUID = -9187935096684609671L;
	private  static final Logger logger = LoggerFactory.getLogger(CreditCalBolt.class);
	private ConfigContainer confContainer;

	/**
	 * 用户在这里实现数量处理逻辑
	 */
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		try{
			Dto4CreditCal indata = (Dto4CreditCal) tuple.getValue(0);
			if (null == indata){
				logger.error("本节点获取数据异常，数据为空");
				return ;
			}
			//解析传入数据
			logger.debug("CreditCalProcesser节点获取数据为：" + indata.toString());
			//信控对象
			OmcObj owner = indata.getOwner();
			
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(indata.getExpanded_info(), JsonObject.class);
			//补充Owner数据
			String extAmount = indata.getAmount();
			String chargingStation = jsonObject.get(OmcCalKey.OMC_CHARGING_STATION).getAsString();
			String chargingPile = jsonObject.get(OmcCalKey.OMC_CHARGING_PILE).getAsString();
			String policyId = jsonObject.get(OmcCalKey.OMC_POLICY_ID).getAsString();

			jsonObject.addProperty(OmcCalKey.OMC_EXT_AMOUNT, extAmount);
			//调用信控计算处理
			OmcCalProcessor omcCalProcessor = new OmcCalProcessor(confContainer,owner,jsonObject);
			omcCalProcessor.process();
			//获取信控计算结果
			JsonObject caldata = omcCalProcessor.getOutput();
			//获取信控计算后的余额信息
			RealTimeBalance realTimeBalance = omcCalProcessor.getRealTimeBalance();

			//根据匹配后的规则列表
			String rules = caldata.get(OmcCalKey.OMC_RULE_ID_LIST).toString();
//			caldata.get(OmcCalKey.OMC_RULE_ID_LIST);
			List<SectionRule> sectionRules = OmcUtils.toSectionRules(confContainer, rules);
			if ((null == sectionRules)||sectionRules.isEmpty()){
				logger.debug("没取到对应的规则，无法信控");
				return ;
			}
			
			logger.debug("--CreditCalProcesser计算完毕");
	
			//准备信控计算后输出
			JsonObject outdata  = new JsonObject();
			outdata.addProperty(OmcCalKey.OMC_CHARGING_STATION, chargingStation);
			outdata.addProperty(OmcCalKey.OMC_CHARGING_PILE, chargingPile);
			outdata.addProperty(OmcCalKey.OMC_POLICY_ID, policyId);
			outdata.add(OmcCalKey.OMC_RULE_ID_LIST, caldata.get(OmcCalKey.OMC_RULE_ID_LIST));
			
		    //准备数据
			Dto4CreditNotice dto4CreditNotice = new Dto4CreditNotice();
			dto4CreditNotice.setOwner(owner);
			dto4CreditNotice.setRealTimeBalance(realTimeBalance);
			dto4CreditNotice.setExtInfo(outdata.toString());

			logger.debug("--dto4CreditNotice--DTO准备完毕：" + dto4CreditNotice.toString());
			// 定义传给下一个bolt所需参数
			collector.emit(new Values(dto4CreditNotice));
		} catch (Exception e) {
			logger.error("----------------------信控计算异常！" , e);
		}

	}

	/**
	 * 用户在这里声明本处理过程输出的字段列表，由流程在执行过程中调用获取
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("dto4CreditNotice"));
	}

	/**
	 * 用户在这里实现处理过程运行前的准备操作
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		try {
			//产生jdbcProxy
			JdbcProxy.loadresource(new JdbcParam(stormConf));
			JdbcProxy.getInstance();
			CacheClient.loadResource(stormConf);

			UrlClient.loadResource(stormConf);
			confContainer = new ConfigContainer();
			confContainer.configObtain();
			confContainer.setSysconfig(stormConf);
		} catch (OmcException e) {
			logger.error("初始化异常",e);
		}
	}

	/**
	 * 用户在这里实现资源清理操作
	 */
	@Override
	public void cleanup() {
	}
}
