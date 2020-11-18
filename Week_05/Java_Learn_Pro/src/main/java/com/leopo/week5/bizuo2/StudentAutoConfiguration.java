package com.leopo.week5.bizuo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

@Configuration
@AutoConfigureBefore(Student.class)
@ConditionalOnProperty(name = "student.enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(Student.class)
public class StudentAutoConfiguration {

    @Autowired
    private Student student;

    @Bean
    @ConditionalOnMissingBean
    public Klass getKlass() {
        Klass klass = new Klass();
        List<Student> students = new ArrayList<>();
        students.add(student);
        klass.setStudents(students);
        return klass;
    }

    @ConditionalOnMissingBean
    public School getSchool() {
        School school = new School();
        List<Klass> klassList = new ArrayList<>();
        Klass klass = getKlass();
        klassList.add(klass);
        school.setKlassList(klassList);
        return school;
    }
}
