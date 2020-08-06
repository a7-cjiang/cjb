package com.fh.controller;

import com.fh.biz.DistrictService;
import com.fh.common.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("district")
public class DistrictController {

    @Autowired
    private DistrictService districtService;


    @GetMapping
    public JsonData getDistrict(){
        String s =districtService.getDistrict();
        return JsonData.success(s);
    }
}
