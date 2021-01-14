package com.example.activemq.jms;

import com.example.activemq.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Consumer {
    private Logger logger = LoggerFactory.getLogger(Consumer.class);
    @JmsListener(destination = "topicTest")
    public void receive(final Map message){
        logger.info("consumer receive :[{}]",message.toString());
    }
}
