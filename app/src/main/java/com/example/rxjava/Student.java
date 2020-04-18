package com.example.rxjava;

import java.util.List;

/**
 * @ProjectName: RxJava2Test
 * @Package: com.example.rxjava
 * @ClassName: Student
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2020/4/15 15:36
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/15 15:36
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Student {
    private String name;
    private int age;
    private List<Course> courses;

    public Student(String name, int age,List<Course> courses) {
        this.name = name;
        this.age = age;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", courses=" + courses +
                '}';
    }

    static class Course {
        private String name;

        public Course(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Course{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
