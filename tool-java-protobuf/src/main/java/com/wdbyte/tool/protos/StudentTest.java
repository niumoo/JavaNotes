package com.wdbyte.tool.protos;

import java.util.Arrays;

import com.wdbyte.tool.protos.StudentOuterClass.Student;

/**
 * @author https://www.wdbyte.com
 * @date 2023/05/10
 */
public class StudentTest {

    public static void main(String[] args) {
        Student student = Student.newBuilder().setId("1").setName("AB").build();
        System.out.println(Arrays.toString(student.toByteArray()));
    }
}
