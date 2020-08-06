package com.fh.controller;

import com.fh.biz.OrderService;
import com.fh.common.JsonData;
import com.fh.common.exception.CountException;
import com.fh.po.OrderProduct;
import com.fh.utils.RedisUse;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping
    public JsonData createOrder(Integer addressId,Integer payType,String flag) throws CountException {
        boolean exists = RedisUse.exists(flag);
        //接口幂等性
        if (exists == true){//true 代表已存在
            return JsonData.error(300,"请勿重复提交订单");
        }else {
            RedisUse.set(flag,flag,10);
        }
        Map map = orderService.createOrder(addressId,payType);
        return JsonData.success(map);
    }

    @GetMapping("createCode")
    private JsonData createCode(Integer orderId,String money) throws Exception {
        Map map = orderService.createCode(orderId,money);
        return JsonData.success(map);
    }

    @GetMapping("queryPayStatus")
    private JsonData queryPayStatus(Integer orderId) throws Exception {
        Integer payStatus = orderService.queryPayStatus(orderId);
        return JsonData.success(payStatus);
    }


    @GetMapping("queryOrderPay")
    private JsonData queryOrderPay(){
        Object list = orderService.queryOrderPay();
        return JsonData.success(list);
    }

    //订单详细信息
    @GetMapping("getOrderDeta")
    public JsonData getOrderDeta(Integer id){
        List list = orderService.getOrderDeta(id);
        return JsonData.success(list);
    }


}
