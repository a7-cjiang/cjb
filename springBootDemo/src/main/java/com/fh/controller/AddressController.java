package com.fh.controller;

import com.fh.biz.AddressService;
import com.fh.common.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public JsonData getAddressVoList(){
        List s = addressService.getAddressVoList();
        return JsonData.success(s);
    }

    //获取收货地址
    @GetMapping("getAddress")
    public JsonData getAddress(){
        List address = addressService.getAddress();
        return JsonData.success(address);
    }

}
