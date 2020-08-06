package com.fh.controller;

import com.fh.biz.MerchandiseService;
import com.fh.common.JsonData;
import com.fh.po.Merchandise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("merchandise")
public class MerchandiseController {

    @Autowired
    private MerchandiseService merchandiseService;


    @GetMapping
    public JsonData getHotMerchandise(){
        String pro = merchandiseService.getHotMerchandise();
        return JsonData.success(pro);
    }


    @GetMapping("getProdecuBytypeId")
    public JsonData getProdecuBytypeId(Integer typeId){
        List<Merchandise> prodecuBytypeId = merchandiseService.getProdecuBytypeId(typeId);
        return JsonData.success(prodecuBytypeId);
    }

    @GetMapping("getProdecuById")
    public JsonData getProdecuById(Integer id){
        Merchandise prodecuById = merchandiseService.getProductById(id);
        return JsonData.success(prodecuById);
    }

}
