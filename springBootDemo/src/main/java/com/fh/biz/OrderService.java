package com.fh.biz;

import com.fh.common.exception.CountException;
import com.fh.po.OrderProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface OrderService {
    Map createOrder(Integer addressId, Integer payType) throws CountException;

    Map createCode(Integer orderId, String money) throws Exception;

    Integer queryPayStatus(Integer orderId) throws Exception;

    Object queryOrderPay();

    List getOrderDeta(Integer id);
}
