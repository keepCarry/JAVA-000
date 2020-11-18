package com.leopo.week5.bizuo2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "student")
public class Student implements Serializable {

    private int id = 1;
    private String name = "student";

    public void init(){
        System.out.println("hello...........");
    }

    public Student create(){
        return new Student();
    }

    public String getName() {
        return name;
    }
}

