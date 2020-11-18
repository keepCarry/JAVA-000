package com.leopo.week5.bizuo1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringTest {


    public static void main(String[] args) {
        // 加载配置文件
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("springbean.xml");
        // 构造方式输出结果
        System.out.println(applicationContext.getBean("student1"));
        // 设值方式输出结果
        System.out.println(applicationContext.getBean("student2"));
    }
}
