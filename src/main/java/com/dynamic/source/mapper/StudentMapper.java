package com.dynamic.source.mapper;

import com.dynamic.source.entity.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMapper {

    public void insertOne(Student bean);

    public Student queryById(Long id);

}
