package com.leopo.week5.bizuo3.dao;

import com.leopo.week5.bizuo3.entity.User;

public interface UserDao {

    void save(User user);

    void update(User user);

    User selectOneById(Long id);

    void deleteById(Long id);
}
