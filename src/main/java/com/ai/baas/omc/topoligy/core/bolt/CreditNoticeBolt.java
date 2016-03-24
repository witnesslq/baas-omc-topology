package com.ai.baas.omc.topoligy.core.bolt;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.ai.baas.omc.topoligy.core.business.NoticeProcessor;
import com.ai.baas.omc.topoligy.core.dto.Dto4CreditNotice;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.pojo.ScoutLog;
import com.ai.baas.omc.topoligy.core.util.CacheClient;
import com.ai.baas.omc.topoligy.core.util.db.JdbcParam;
import com.ai.baas.omc.topoligy.core.util.db.JdbcProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import backtype.storm.topology.base.BaseBasicBolt;

/**
 * 信用通知
 * 
 * @author liutong
 */

public class CreditNoticeBolt extends BaseBasicBolt{
	
	private static final long serialVersionUID = 8800273670460388592L;
	private  static final Logger logger = LoggerFactory.getLogger(CreditNoticeBolt.class);
	private ConfigContainer confContainer;
	/**
	 * 用户在这里实现数量处理逻辑
	 */
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		try {
			Dto4CreditNotice dto4CreditNotice = (Dto4CreditNotice) tuple.getValue(0);
			logger.info("--------CreditNoticeProcesser-----------接受到DTO：" + dto4CreditNotice);
			
			ScoutLog data = new ScoutLog();
			data.setOwner(dto4CreditNotice.getOwner());
			data.setRealTimeBalance(dto4CreditNotice.getRealTimeBalance());

			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(dto4CreditNotice.getExtInfo(), JsonObject.class);

			//信控通知初始化
			NoticeProcessor noticeProcessor = new NoticeProcessor(
					confContainer,dto4CreditNotice.getOwner(),jsonObject,dto4CreditNotice.getRealTimeBalance());
			//信控通知处理
			noticeProcessor.process();
		} catch (Exception e) {
			logger.error("--------------------------------信用通知模块出现异常", e);
		}
	}

	/**
	 * 用户在这里声明本处理过程输出的字段列表，由流程在执行过程中调用获取
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("dto4CommSend"));
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

			//加载缓存资源
			CacheClient.loadResource(stormConf);
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
	
	public void cleanup() {
	}
	
}
