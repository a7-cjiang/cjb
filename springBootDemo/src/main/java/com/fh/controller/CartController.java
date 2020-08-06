package com.fh.controller;

import com.fh.biz.CartService;
import com.fh.common.JsonData;
import com.fh.po.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("addToCart")
    public JsonData addToCart(Integer id,Integer count){
        Integer count_type = cartService.addProToCart(id,count);
        return JsonData.success(count_type);
    }

    @GetMapping
    public JsonData getCartPro(){
        List cartVoList = cartService.getCartPro();
        return JsonData.success(cartVoList);
    }

    @GetMapping("updateCartProStatus")
    public JsonData updateCartProStatus(String ids){
        cartService.updateCartProStatus(ids);
        return JsonData.success("修改成功");
    }

    @GetMapping("deleteCartPro")
    public JsonData deleteCartPro(Integer id){
        cartService.deleteCartPro(id);
        return JsonData.success("删除成功");
    }

    @GetMapping("getCheckCartStatus")
    public JsonData getCheckCartStatus(){
        List s = cartService.getCheckCartStatus();
        return JsonData.success(s);
    }
}
