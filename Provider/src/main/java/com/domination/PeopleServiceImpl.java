package com.domination;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PeopleServiceImpl implements PeopleService {
    private static final Map<String, People> peopleMap = new ConcurrentHashMap<>();

    @Override
    public String createPerson(String name, int age) {
        People p = new People(name, age);
        String id = UUID.randomUUID().toString();
        peopleMap.put(id, p);
        return id;
    }

    @Override
    public void setAge(String id, int age) {
        People p = peopleMap.get(id);
        if (p != null) p.setAge(age);
    }

    @Override
    public int getAge(String id) {
        People p = peopleMap.get(id);
        return p != null ? p.getAge() : -1;
    }
}