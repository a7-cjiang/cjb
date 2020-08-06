package com.fh.controller;

import com.fh.biz.TypeService;
import com.fh.common.JsonData;
import com.fh.po.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping
    public JsonData getType(){
        String type = typeService.getType();
        return JsonData.success(type);
    }
}
