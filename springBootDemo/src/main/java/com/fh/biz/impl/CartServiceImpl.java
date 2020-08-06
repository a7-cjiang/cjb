package com.fh.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.biz.CartService;
import com.fh.mapper.MerchandiseMapper;
import com.fh.po.Merchandise;
import com.fh.po.ShopUserVip;
import com.fh.po.vo.CartVo;
import com.fh.utils.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MerchandiseMapper merchandiseMapper;


    @Override
    public Integer addProToCart(Integer id,Integer count) {

        //先判断库存是否充足
        Merchandise productById = merchandiseMapper.getProductById(id);
        if (count > productById.getStock()){
            return count-productById.getStock();
        }

        //获取当前登录的用户的手机号
        Map login_user = (Map) request.getAttribute("login_user");
        String phone = (String) login_user.get("phone");

        //查看redis中是否有该商品
        String hget = RedisUse.hget("cart_" + phone + "_ssn", id + "");

        //为空则没有
        if (StringUtils.isBlank(hget)){
            CartVo cartVo = merchandiseMapper.getProToCartById(id);

            cartVo.setCheck(true);
            cartVo.setCount(count);
            cartVo.setMoney(cartVo.getPrice().multiply(new BigDecimal(count)));

            //转换为json字符串并存人redis
            String s = JSONObject.toJSONString(cartVo);
            RedisUse.hset("cart_" + phone + "_ssn", id + "",s);
        }else {//该商品已存在于购物车中    修改商品数量和小计

            //将json字符串转换为javabean
            CartVo cartVo = JSONObject.parseObject(hget, CartVo.class);

            //验证库存是否充足
            if (cartVo.getCount()+count > productById.getStock()){
                return cartVo.getCount()+count - productById.getStock();
            }

            cartVo.setCount(cartVo.getCount()+count);

            if (cartVo.getCount() <= 1){
                cartVo.setCount(1);
            }

            cartVo.setMoney(cartVo.getPrice().multiply(new BigDecimal(cartVo.getCount())));

            //转换为json字符串并存人redis
            String s = JSONObject.toJSONString(cartVo);
            RedisUse.hset("cart_" + phone + "_ssn", id + "",s);
        }

        //获取商品的个数
        Long hlen = RedisUse.hlen("cart_" + phone + "_ssn");

        return Integer.valueOf(String.valueOf(hlen));
    }

    @Override
    public List getCartPro() {
        //获取当前登录的用户的手机号
        Map login_user = (Map) request.getAttribute("login_user");
        String phone = (String) login_user.get("phone");

        List<String> hvals = RedisUse.hvals("cart_" + phone + "_ssn");
        return hvals;
    }

    @Override
    public void updateCartProStatus(String ids) {
        //获取当前登录的用户的手机号
        Map login_user = (Map) request.getAttribute("login_user");
        String phone = (String) login_user.get("phone");

        //查询购物车中的商品
        List<String> cart = RedisUse.hvals("cart_" + phone + "_ssn");

        for (int i = 0; i < cart.size(); i++) {
            CartVo cartVo = JSONObject.parseObject(cart.get(i), CartVo.class);

            Integer id = cartVo.getId();

            if (ids.contains(id+",")){
                cartVo.setCheck(true);
            }else {
                cartVo.setCheck(false);
            }

            //修改好状态之后存入redis
            RedisUse.hset("cart_" + phone + "_ssn",id+"",JSONObject.toJSONString(cartVo));
        }
    }

    @Override
    public void deleteCartPro(Integer id) {
        //获取当前登录的用户的手机号
        Map login_user = (Map) request.getAttribute("login_user");
        String phone = (String) login_user.get("phone");

        RedisUse.hdel("cart_" + phone + "_ssn",id+"");
    }

    @Override
    public List getCheckCartStatus() {
        //获取当前登录的用户的手机号
        Map login_user = (Map) request.getAttribute("login_user");
        String phone = (String) login_user.get("phone");

        List<String> cart = RedisUse.hvals("cart_" + phone + "_ssn");

        List list = new ArrayList();

        for (int i = 0; i < cart.size(); i++) {
            CartVo cartVo = JSONObject.parseObject(cart.get(i), CartVo.class);
            if (cartVo.getCheck() == true){
                list.add(cartVo);
            }
        }

        return list;
    }
}
