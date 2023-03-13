package com.spring.tobbyspringv1example.doamin;

import com.spring.tobbyspringv1example.dao.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
}
