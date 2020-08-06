package com.fh.controller;

import com.fh.biz.UserService;
import com.fh.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public List<User> getUser(){
        List<User> list = userService.getUser();
        return list;
    }

    @PostMapping
    public String postUser(User user){
        userService.postUser(user);
        return "POST SUCCESS";
    }

    @DeleteMapping
    public String deleteUser(Integer id){
        userService.deleteUser(id);
        return "DELETE SUCCESS";
    }

    @PutMapping
    public String putMapping(User user){
        userService.putMapping(user);
        return "PUT SUCCESS";
    }


}
