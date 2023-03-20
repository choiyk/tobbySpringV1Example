package com.spring.tobbyspringv1example.user.dao;

import com.spring.tobbyspringv1example.user.domain.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
}
