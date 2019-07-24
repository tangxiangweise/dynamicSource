package com.dynamic.source;

import com.dynamic.source.entity.Student;
import com.dynamic.source.mapper.StudentMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DynamicSourceApplication.class)
public class DynamicSourceApplicationTest {

    @Autowired
    public StudentMapper studentMapper;

    @Test
    public void student() {
        Student student = new Student();
        student.setName("txw");
        studentMapper.insertOne(student);
    }


    @Test
    public void getStudent() {
        Student student = studentMapper.queryById(1L);
        System.out.println("..........." + student);
    }


}
