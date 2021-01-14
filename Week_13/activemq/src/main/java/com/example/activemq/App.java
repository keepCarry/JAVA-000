package com.example.activemq;

import com.example.activemq.jms.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Administrator
 * @description:
 */
@SpringBootApplication
@EnableJms
public class App implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(App.class);
    @Autowired
    private Producer producer;


    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        String topic = "topicTest";
        Map<String, String> message = new HashMap<>(1);
        message.put("messageKey", "messageValue");
        logger.info("producer send message to topic " + topic + "[{}]" ,message );
        producer.sendMessage(topic, message);
    }
}
