package com.ai.baas.omc.topoligy.core.test;


import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.google.gson.JsonObject;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.metrics.stats.SampledStat;
import org.junit.Test;

import java.util.Properties;

/**
 * Created by jackieliu on 16/3/24.
 */
public class KafkaProduceTest {
    //与配置文件中kafka.spout.topic一致
    private static final String OMC_TOPIC = "ECBCA29571714183B23A630E2311DD66_MDS002_1529549922";

    /**
     * 想kafka的信控topic中写入信息
     */
    private void produceOmc(String strMsg){
        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092");
        props.put("bootstrap.servers", "10.1.245.5:39091,10.1.245.6:39091,10.1.245.7:39091");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String,String> producer = new KafkaProducer<String,String>(props);
        producer.send(
                new ProducerRecord<String, String>(OMC_TOPIC, strMsg));

        producer.close();
    }

    /**
     * 模拟停止操作
     */
    @Test
    public void vdStop(){
        //信控数据源信息
        JsonObject jsonObject = new JsonObject();
        //数量
        jsonObject.addProperty("amount", "30");//0
        //acct:账户；cust:客户；subs:用户
        jsonObject.addProperty("owner_type", "subs");
        //属主id
        jsonObject.addProperty("owner_id", "5211314");
        //数量的类型 VOICE:语音资源;DATA:流量资源;SM:短信资源;VC:虚拟币资源;BOOK:资金账本;PC:电量资源
        jsonObject.addProperty("amount_type", "DATA");
        //事件类型：CASH主业务（按资料信控），VOICE 语音，SMS 短信，DATA 数据
        jsonObject.addProperty("event_type", "CASH");//DATA
        //数量的增减属性，包括PLUS(导致余额增加的，如缴费导致的)，MINUS(导致余额减少的，如业务使用导致的)
        jsonObject.addProperty("amount_mark", "MINUS");//PLUS
        //来源 resource：资源入账，bmc：计费
        jsonObject.addProperty("source_type", "bmc");
        //租户id
        jsonObject.addProperty("tenant_id", "VIV-BYD");
        //系统id
        jsonObject.addProperty("system_id", "VIV");
        //事件id
        jsonObject.addProperty("event_id", Long.toString(System.currentTimeMillis()));
        //扩展信息，用json传递具体信息
        jsonObject.addProperty("expanded_info", "");

        produceOmc(jsonObject.toString());
    }
}
/*//数量
jsonObject.addProperty("amount", "30");
//acct:账户；cust:客户；subs:用户
jsonObject.addProperty("owner_type", "subs");
//属主id
jsonObject.addProperty("owner_id", "50");
//数量的类型 VOICE:语音资源;DATA:流量资源;SM:短信资源;VC:虚拟币资源;BOOK:资金账本;PC:电量资源
jsonObject.addProperty("amount_type", "DATA");
//事件类型：CASH主业务（按资料信控），VOICE 语音，SMS 短信，DATA 数据
jsonObject.addProperty("event_type", "CASH");
//数量的增减属性，包括PLUS(导致余额增加的，如缴费导致的)，MINUS(导致余额减少的，如业务使用导致的)
jsonObject.addProperty("amount_mark", "MINUS");
//来源 resource：资源入账，bmc：计费
jsonObject.addProperty("source_type", "bmc");
//租户id
jsonObject.addProperty("tenant_id", "VIV-BYD");
//系统id
jsonObject.addProperty("system_id", "VIV");
//事件id
jsonObject.addProperty("event_id", Long.toString(System.currentTimeMillis()));
*/