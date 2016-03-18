package com.ai.baas.omc.topoligy.core.bolt;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	private JdbcProxy jdbcproxy;

	/**
	 * 用户在这里实现数量处理逻辑
	 */
	@Override
	public void execute(StreamData adata) {
		try{
			Dto4CreditCal indata = (Dto4CreditCal) adata.getValue(0);
			
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
			//根据匹配后的规则列表判断是否需要通知
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
			//TODO准备数据
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
			List<StreamData> a = new ArrayList<StreamData>();
			a.add(adata);
			super.setValues(new Values(dto4CreditNotice));
			super.ack();
		} catch (Exception e) {
			logger.error("----------------------信控计算异常！" , e);
		}

	}

	/**
	 * 用户在这里声明本处理过程输出的字段列表，由流程在执行过程中调用获取
	 */
//	@Override
	public Fields getOutFields() {
		return new Fields("dto4CreditNotice");
	}

	/**
	 * 用户在这里实现处理过程运行前的准备操作
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map aConf, FlowContext aContext, ProcessorCollector collector) {
		
		try {
			JdbcProxy.loadresource(new JdbcParam(aConf));
			JdbcProxy jdbcProxy = JdbcProxy.getInstance();
			setJdbcproxy(jdbcProxy);
			CacheClient.loadResource(aConf);
			UrlClient.loadResource(aConf);
			confContainer = new ConfigContainer();
			confContainer.configObtain();
			confContainer.setSysconfig(aConf);
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

	@Override
	public void buildLogger(Logger LOG) {
//		Logger logger = LoggerFactory.getLogger(CreditCalProcesser.class);
//		LOG = logger;
	}

	public JdbcProxy getJdbcproxy() {
		return jdbcproxy;
	}

	public void setJdbcproxy(JdbcProxy jdbcproxy) {
		this.jdbcproxy = jdbcproxy;
	}
	
}
