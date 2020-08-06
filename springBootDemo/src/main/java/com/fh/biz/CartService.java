package com.fh.biz;

import com.fh.po.vo.CartVo;

import java.util.List;

public interface CartService {
    Integer addProToCart(Integer id,Integer count);

    List getCartPro();

    void updateCartProStatus(String ids);

    void deleteCartPro(Integer id);

    List getCheckCartStatus();
}
