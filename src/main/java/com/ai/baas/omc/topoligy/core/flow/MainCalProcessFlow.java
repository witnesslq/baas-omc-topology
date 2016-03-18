package com.ai.baas.omc.topoligy.core.flow;

import com.ai.baas.omc.topoligy.core.bolt.CreditCalBolt;
import com.ai.baas.omc.topoligy.core.bolt.CreditNoticeBolt;
import com.ai.baas.omc.topoligy.core.bolt.RealEventBolt;
import com.ai.baas.storm.flow.BaseFlow;
import com.ai.baas.storm.util.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuTong
 */
public class MainCalProcessFlow extends BaseFlow {
	private static final Logger logger = LoggerFactory.getLogger(MainCalProcessFlow.class);

	@Override
	public void define() {
		//设置kafka输入源
		super.setKafkaSpout();
		builder.setBolt("realEvent-bolt", new RealEventBolt(), 1).shuffleGrouping(BaseConstants.KAFKA_SPOUT_NAME);
		builder.setBolt("creditCal-bolt", new CreditCalBolt(), 1).shuffleGrouping("realEvent-bolt");
		builder.setBolt("creditNotice-bolt", new CreditNoticeBolt(), 1).shuffleGrouping("creditCal-bolt");
	}

	public static void main(String[] args){
		MainCalProcessFlow flow = new MainCalProcessFlow();
		flow.run(args);
	}
}
