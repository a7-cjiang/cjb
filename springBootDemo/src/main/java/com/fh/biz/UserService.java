package com.fh.biz;

import com.fh.po.User;

import java.util.List;

public interface UserService {
    List<User> getUser();

    void postUser(User user);

    void deleteUser(Integer id);

    void putMapping(User user);
}
