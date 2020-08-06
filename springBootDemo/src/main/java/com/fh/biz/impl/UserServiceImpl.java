package com.fh.biz.impl;

import com.fh.biz.UserService;
import com.fh.mapper.UserMapper;
import com.fh.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public List<User> getUser() {
        return userMapper.selectList(null);
    }

    @Override
    public void postUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    public void putMapping(User user) {
        userMapper.updateById(user);
    }
}
