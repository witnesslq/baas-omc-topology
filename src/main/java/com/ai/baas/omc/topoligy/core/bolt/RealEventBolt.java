package com.ai.baas.omc.topoligy.core.bolt;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.ai.baas.omc.topoligy.core.business.EventProcessor;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.dto.Dto4CreditCal;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.util.CacheClient;
import com.ai.baas.omc.topoligy.core.util.db.JdbcParam;
import com.ai.baas.omc.topoligy.core.util.db.JdbcProxy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 信控参数计算，策略和规则获取
 * @author jackieliu
 */
public class RealEventBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 5454304868861020989L;
	private  static final Logger logger = LoggerFactory.getLogger(RealEventBolt.class);
	private ConfigContainer confContainer;

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		try {
			logger.info("adata = [" + tuple.getMessageId() + "]");
			//获取输入数据
			String data = tuple.getString(0);
			logger.info("adata = [" + data + "]");

			Gson gson = new Gson();
			//对获取数据进行解析
			JsonObject input = gson.fromJson(data, JsonObject.class);
			String amount = input.get("amount").getAsString();
			String owner_type = input.get("owner_type").getAsString();
			String amount_type = input.get("amount_type").getAsString();
			String event_type = input.get("event_type").getAsString();
			String amount_mark = input.get("amount_mark").getAsString();
			String owner_id = input.get("owner_id").getAsString();
			String source_type = input.get("source_type").getAsString();
			String tenant_id = input.get("tenant_id").getAsString();
			String system_id = input.get("system_id").getAsString();
			String event_id = input.get("event_id").getAsString();
			String expanded_info = input.get("expanded_info").toString();

			if (!owner_type.startsWith("/")){
				owner_type = "/" + owner_type;
			}
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(OmcCalKey.OMC_SYSTEM_ID, system_id);
			jsonObject.addProperty(OmcCalKey.OMC_CHARGING_STATION, "");
			jsonObject.addProperty(OmcCalKey.OMC_CHARGING_PILE, "");

			logger.debug("策略、规则、参数信息获取......");
			//信控对象
			OmcObj obj = new OmcObj(tenant_id, owner_type,owner_id,event_type);

			//获取策略、规则
			EventProcessor eventProcessor = new EventProcessor(confContainer, obj,jsonObject);
			eventProcessor.process();
			JsonObject evendata = eventProcessor.getOutput();

			//添加策略信息
			jsonObject.add(eventProcessor.DF_POLICY, evendata.get(eventProcessor.DF_POLICY));
			//添加策略对应所有规则信息
			jsonObject.add(eventProcessor.DF_RULES, evendata.get(eventProcessor.DF_RULES));

			Dto4CreditCal dto4CreditCal = new Dto4CreditCal();
			dto4CreditCal.setOwner(obj);
			dto4CreditCal.setAmount(amount);
			dto4CreditCal.setAmount_mark(amount_mark);
			dto4CreditCal.setAmount_type(amount_type);
			dto4CreditCal.setEventid(event_id);
			dto4CreditCal.setEventtype(event_type);
			dto4CreditCal.setExpanded_info(jsonObject.toString());
			logger.info("--------------------------------传输给下一节点DTO：" + dto4CreditCal.toString());
			// 定义传给下一个bolt所需参数
			collector.emit(new Values(dto4CreditCal));
		} catch(OmcException e){
			logger.error("---------------------信控事件处理异常" ,e);
		} catch (Exception e) {
			logger.error("---------------------信控异常" , e);
		}
		
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("dto4CreditCal"));
	}

	/**
	 * 初始化相关参数和实例
	 * @param stormConf
	 * @param context
     */
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		try {
			//产生jdbcProxy
			JdbcProxy.loadresource(new JdbcParam(stormConf));
			JdbcProxy.getInstance();
//			setJdbcproxy(JdbcProxy.getInstance());
			CacheClient.loadResource(stormConf);

			confContainer = new ConfigContainer();
			confContainer.configObtain();
			confContainer.setSysconfig(stormConf);
		} catch (OmcException e) {
			logger.error("初始化异常",e);
		}
	}
}
