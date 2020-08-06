package com.fh.biz.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fh.biz.OrderService;
import com.fh.common.PayStatusEnum;
import com.fh.common.exception.CountException;
import com.fh.mapper.MerchandiseMapper;
import com.fh.mapper.OrderMapper;
import com.fh.mapper.OrderProductMapper;
import com.fh.mapper.VipMapper;
import com.fh.po.Merchandise;
import com.fh.po.Order;
import com.fh.po.OrderProduct;
import com.fh.po.ShopUserVip;
import com.fh.po.vo.CartVo;
import com.fh.utils.PriceConversion;
import com.fh.utils.RedisUse;
import com.fh.utils.WxPayUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private VipMapper vipMapper;
    @Autowired
    private MerchandiseMapper merchandiseMapper;


    @Override
    public Map createOrder(Integer addressId, Integer payType) throws CountException {
        //获取当前登录的用户的手机号
        Map login_user = (Map) request.getAttribute("login_user");
        String phone = (String) login_user.get("phone");

        Map map = new HashMap();

        Order order = new Order();

        order.setPayType(payType);
        order.setAddressId(addressId);
        order.setCreateDate(new Date());
        order.setPayStatus(PayStatusEnum.PAY_STATUS_INIT.getStatus());

        Integer typeCount = 0;
        BigDecimal totalMoney = new BigDecimal(0);

        //查询购物车中的商品
        List<String> cart = RedisUse.hvals("cart_" + phone + "_ssn");

        List<OrderProduct> list = new ArrayList<>();

        for (int i = 0; i < cart.size(); i++) {
            CartVo cartVo = JSONObject.parseObject(cart.get(i), CartVo.class);

            if (cartVo.getCheck() == true){
                //商品信息
                Merchandise merchandise = merchandiseMapper.selectById(cartVo.getId());
                //库存是否充足
                if (merchandise.getStock() >= cartVo.getCount()){
                    typeCount ++;
                    totalMoney = totalMoney.add(cartVo.getMoney());
                    //订单详情表
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setProductId(cartVo.getId());
                    orderProduct.setCount(cartVo.getCount());

                    list.add(orderProduct);

                    //减库存，数据库锁，保证不会超卖
                    int i1 = merchandiseMapper.updateProductCount(merchandise.getId().intValue(),cartVo.getCount());
                    if (i1 == 0){//超卖
                        throw new CountException("商品编号为"+cartVo.getId()+"的商品库存不足，库存只有"+merchandise.getStock());
                    }
                }else {//库存不足
                    throw new CountException("商品编号为"+cartVo.getId()+"的商品库存不足，库存只有"+merchandise.getStock());
                }
            }
        }
        if (phone.length() == 11){
            ShopUserVip vip = vipMapper.getVipByNumber(phone);
            order.setVipId(vip.getId());
        }else {
            ShopUserVip vip = vipMapper.getVipByName(phone);
            order.setVipId(vip.getId());
        }

        order.setProductCount(typeCount);
        order.setTotalMoney(totalMoney);

        //生成订单
        orderMapper.insert(order);

        orderProductMapper.batchAdd(list,order.getId());

        //订单中已经结算的商品，从redis中删除
        for (int i = 0; i < cart.size(); i++) {
            //转换为javabean
            CartVo cartVo = JSONObject.parseObject(cart.get(i), CartVo.class);

            if (cartVo.getCheck()== true){
                RedisUse.hdel("cart_" + phone + "_ssn",cartVo.getId()+"");
            }

            map.put("orderId",order.getId());
            map.put("totalMoney",totalMoney);

        }

        return map;
    }


    @Override
    public Map createCode(Integer orderId, String money) throws Exception {
        Map map = new HashMap();
        String s1 = RedisUse.get("order_ssn_" + orderId);
        //如果redis中已有二维码路径，直接返回
        if (StringUtils.isEmpty(s1) != true){
            map.put("code",200);
            map.put("code_url",s1);
            return map;
        }
        String s = PriceConversion.YuanZhuanFen(money);
        Map resp = WxPayUtil.createErCode((long)orderId, s);

        if("SUCCESS".equalsIgnoreCase((String) resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase((String) resp.get("result_code"))){ //true说明生成二维码成功
            map.put("code",200);
            map.put("code_url",resp.get("code_url"));

            //更改支付状态
            Order order = new Order();
            order.setId(orderId);
            order.setPayStatus(PayStatusEnum.PAY_STATUS_Wait.getStatus());
            orderMapper.updateById(order);

            //二维码路径存入缓存
            RedisUse.set("order_ssn_" + orderId, String.valueOf(resp.get("code_url")),60*30);
        }else {
            map.put("code",501);
            map.put("data",resp.get("return_msg"));
        }

        return map;
    }

    @Override
    public Integer queryPayStatus(Integer orderId) throws Exception {
        Map resp = WxPayUtil.queryPayStatus(orderId);
        if ("SUCCESS".equalsIgnoreCase((String) resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase((String) resp.get("result_code"))){
            if("SUCCESS".equalsIgnoreCase(String.valueOf(resp.get("trade_state")))){//支付成功
                //更新订单状态
                Order order=new Order();
                order.setId(orderId);
                order.setPayStatus(PayStatusEnum.PAY_STATUS_SUCCESS.getStatus());
                orderMapper.updateById(order);
                return 1;
            }else if("USERPAYING".equalsIgnoreCase(String.valueOf(resp.get("trade_state")))){//用户支付中
                Order order=new Order();
                order.setId(orderId);
                order.setPayStatus(PayStatusEnum.PAY_STATUS_ING.getStatus());
                orderMapper.updateById(order);
                return 2;
            }else if("CLOSED".equalsIgnoreCase((String) resp.get("trade_state"))){//已关闭
                Order order=new Order();
                order.setId(orderId);
                order.setPayStatus(PayStatusEnum.PAY_STATUS_Error.getStatus());
                return 3;
            }
        }
        return 0;
    }

    @Override
    public Object queryOrderPay() {
        Map login_user = (Map) request.getAttribute("login_user");
        String phone = (String) login_user.get("phone"); //phone 登录的名字

        if (phone.length() == 11){
            ShopUserVip vip = vipMapper.getVipByNumber(phone);
            List<Order> orderByPhone = orderMapper.getOrderByPhone(String.valueOf(vip.getId()));
            Object o1 = JSONArray.toJSON(orderByPhone);
            return o1;
        }else {
            ShopUserVip vip = vipMapper.getVipByName(phone);
            List<Order> orderByPhone = orderMapper.getOrderByPhone(String.valueOf(vip.getId()));
            Object o1 = JSONArray.toJSON(orderByPhone);
            return o1;
        }
        //list转json
//        Object o = JSONArray.toJSON(list);
    }

    @Override
    public List getOrderDeta(Integer id) {

        List orderProduct = orderProductMapper.getOrderDeta(id);

        for (int i = 0; i < orderProduct.size(); i++) {
            String s = JSONObject.toJSONString(orderProduct.get(i));
            System.out.println(s);
        }
        return orderProduct;
    }
}
