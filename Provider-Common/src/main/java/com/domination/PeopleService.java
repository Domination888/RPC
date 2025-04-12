package com.domination;

public interface PeopleService {
    String createPerson(String name, int age); // 返回一个对象ID
    void setAge(String id, int age);
    int getAge(String id);
}

