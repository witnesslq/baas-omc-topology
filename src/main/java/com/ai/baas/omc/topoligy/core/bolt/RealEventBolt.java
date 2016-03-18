package com.ai.baas.omc.topoligy.core.bolt;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.ai.baas.omc.topoligy.core.dto.Dto4CreditCal;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.util.db.JdbcProxy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 信控参数计算，策略计算
 * @author jackieliu
 */
public class RealEventBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 5454304868861020989L;
	private  static final Logger logger = LoggerFactory.getLogger(RealEventBolt.class);
	private ConfigContainer confContainer;
	private JdbcProxy jdbcproxy;

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		try {
			logger.error("adata = [" + tuple.getMessageId() + "]");
			//获取输入数据
			String data = tuple.getString(0);
			logger.info("adata = [" + data + "]");
			Gson gson = new Gson();
			//对获取数据进行解析
			JsonObject input = gson.fromJson(data, JsonObject.class);
			//数量
			String amount = input.get("amount").getAsString();
			//acct:账户；cust:客户；subs:用户
			String owner_type = input.get("owner_type").getAsString();
			//数量的类型：
			String amount_type = input.get("amount_type").getAsString();
			//事件类型：CASH主业务（按资料信控），VOICE 语音，SMS 短信，DATA 数据
			String event_type = input.get("event_type").getAsString();
			//数量的增减属性，包括PLUS(导致余额增加的，如缴费导致的)，MINUS(导致余额减少的，如业务使用导致的)
			String amount_mark = input.get("amount_mark").getAsString();
			//属主id
			String owner_id = input.get("owner_id").getAsString();
			//来源 resource：资源入账，bmc：计费
			String source_type = input.get("source_type").getAsString();
			//租户id
			String tenant_id = input.get("tenant_id").getAsString();
			//系统id
			String system_id = input.get("system_id").getAsString();
			//事件id
			String event_id = input.get("event_id").getAsString();
			//扩展信息，用json传递具体信息
			String expanded_info = input.get("expanded_info").toString();

			if (!owner_type.startsWith("/")){
				owner_type = "/" + owner_type;
			}
			JsonObject jsonObject = new JsonObject();
			//获取配置信息
			Map<String,String> syscfg =  confContainer.getSysconfig();
			String projectname = syscfg.get(OmcCalKey.OMC_CFG_PROJECTNAME);
			jsonObject.addProperty(OmcCalKey.OMC_SYSTEM_ID, system_id);
			//区分项目
			//充电桩项目
			if (PROJECTNAME.CLC.equals(projectname)){
				Validity validity = new ValidityClc();
				validity.inputDateExpandedinfoCheck(source_type, expanded_info);
				jsonObject.addProperty(OmcCalKey.OMC_CHARGING_STATION, "");
				jsonObject.addProperty(OmcCalKey.OMC_CHARGING_PILE, "");
				//车联网项目
			}else if(PROJECTNAME.VIV.equals(projectname)){
				Validity validity = new ValidityVlv();
				validity.inputDateExpandedinfoCheck(source_type, expanded_info);
				jsonObject.addProperty(OmcCalKey.OMC_CHARGING_STATION, "");
				jsonObject.addProperty(OmcCalKey.OMC_CHARGING_PILE, "");
			}

			logger.debug("策略、规则、参数信息获取......");
			//信控对象
			OmcObj obj = new OmcObj(tenant_id, owner_type,owner_id,event_type);
			//获取策略、规则
			EventProcessor eventProcessor = new EventProcessor(confContainer, obj,jsonObject);
			eventProcessor.process();
			JsonObject evendata = eventProcessor.getOutput();
//
			//添加策略信息
			jsonObject.add(eventProcessor.DF_POLICY, evendata.get(eventProcessor.DF_POLICY));
			//添加策略对应所有规则信息
			jsonObject.add(eventProcessor.DF_RULES, evendata.get(eventProcessor.DF_RULES));

//
			Dto4CreditCal dto4CreditCal = new Dto4CreditCal();
			dto4CreditCal.setOwner(obj);
			dto4CreditCal.setAmount(amount);
			dto4CreditCal.setAmount_mark(amount_mark);
			dto4CreditCal.setAmount_type(amount_type);
			dto4CreditCal.setEventid(event_id);
			dto4CreditCal.setEventtype(event_type);
			dto4CreditCal.setExpanded_info(jsonObject.toString());


			logger.error("--------------------------------传输给下一节点DTO：" + dto4CreditCal.toString());
			// 定义传给下一个bolt所需参数
			List<StreamData> a = new ArrayList<StreamData>();
			a.add(adata);
			super.setValues(new Values(dto4CreditCal));
			super.ack();

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
		super.prepare(stormConf, context);
	}
}
