package com.leopo.week5.bizuo1.vo;

import org.springframework.stereotype.Component;

@Component
public class AutoWiredBean {

    public AutoWiredBean() {
        System.out.println("AutoWiredBeane已加载");
    }

    public void testBean() {
        System.out.println("123456");
    }
}
