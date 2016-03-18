package com.ai.baas.omc.topoligy.core.bolt;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import backtype.storm.topology.base.BaseBasicBolt;

/**
 * 信用通知
 * 
 * @author guofei
 */

public class CreditNoticeBolt extends BaseBasicBolt{
	
	private static final long serialVersionUID = 8800273670460388592L;
	private  static final Logger logger = LoggerFactory.getLogger(CreditNoticeBolt.class);
	private ConfigContainer confContainer;
	private JdbcProxy jdbcproxy;
	/**
	 * 用户在这里实现数量处理逻辑
	 */
	@Override
	public void execute(StreamData adata) {
		try {
			Dto4CreditNotice dto4CreditNotice = (Dto4CreditNotice) adata.getValue(0);
			logger.info("--------CreditNoticeProcesser-----------接受到DTO：" + dto4CreditNotice);
			
			ScoutLog data = new ScoutLog();
			data.setOwner(dto4CreditNotice.getOwner());
			data.setRealTimeBalance(dto4CreditNotice.getRealTimeBalance());

			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(dto4CreditNotice.getExtInfo(), JsonObject.class);

			//信控通知初始化
			NoticeProcessor noticeProcessor = new NoticeProcessor(confContainer,dto4CreditNotice.getOwner(),jsonObject,dto4CreditNotice.getRealTimeBalance());
			//信控通知处理
			noticeProcessor.process();
			
			super.ack();
			
		} catch (Exception e) {
			logger.error("--------------------------------信用通知模块出现异常", e);
		}
	}

	/**
	 * 用户在这里声明本处理过程输出的字段列表，由流程在执行过程中调用获取
	 */
	@Override
	public Fields getOutFields() {
		return new Fields("dto4CommSend");
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
	
	public void cleanup() {
	}

	@Override
	public void buildLogger(Logger LOG) {
//		Logger logger = LoggerFactory.getLogger(CreditNoticeProcesser.class);
//		LOG = logger;		
	}

	public JdbcProxy getJdbcproxy() {
		return jdbcproxy;
	}

	public void setJdbcproxy(JdbcProxy jdbcproxy) {
		this.jdbcproxy = jdbcproxy;
	}
	
	
}
