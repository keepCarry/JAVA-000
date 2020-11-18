package com.leopo.week5.bizuo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.leopo.week5.bizuo1.vo.AutoWiredBean;


@Configuration
@ComponentScan("com.leopo.week5.bizuo1.vo")
public class AutoBeanConfig {

    @Autowired
    private AutoWiredBean bean;

    @Bean
    public void autoBeanTest() {
    }

    public static void main(String[] args) {
        //创建容器
        AnnotationConfigApplicationContext annotationConfigApplicationContext
                = new AnnotationConfigApplicationContext(AutoBeanConfig.class);
        //获得所有Bean的名字
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }

}
