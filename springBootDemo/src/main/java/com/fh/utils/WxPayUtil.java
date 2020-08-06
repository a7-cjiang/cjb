package com.fh.utils;

import com.fh.github.wxpay.sdk.FeiConfig;
import com.fh.github.wxpay.sdk.WXPay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WxPayUtil {

    // 微信支付  natvie   商户生成二维码
    public static Map createErCode(Long orderId, String money) throws Exception {

        //配置配置信息
        FeiConfig config = new FeiConfig();
        //得到微信支付对象
        WXPay wxpay = new WXPay(config);
        //设置请求参数
        Map<String, String> data = new HashMap<String, String>();
        //对订单信息描述
        data.put("body", "宁哥商城");
        //String payId = System.currentTimeMillis()+"";
        //设置订单号 （保证唯一 ）
        data.put("out_trade_no","Wx_order_ssn"+orderId);
        //设置币种
        data.put("fee_type", "CNY");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date d=new Date();
        String dateStr = sdf.format(new Date(d.getTime() + 120000000));
        //设置二维码的失效时间
        data.put("time_expire", dateStr);
        //设置订单金额   单位分
        data.put("total_fee",money);
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        //设置支付方式
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        // 统一下单
        Map<String, String> resp = wxpay.unifiedOrder(data);
        String code = resp.get("code_url");
        System.out.println("-------------------------------------------------------------------"+resp.get("code_url"));
//        System.out.println(JSONObject.toJSONString(resp));
        return resp;
    }


    public static Map queryPayStatus(Integer orderId) throws Exception {
        FeiConfig config = new FeiConfig();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no","Wx_order_ssn"+orderId);
        // 查询支付状态
        Map<String, String> resp = wxpay.orderQuery(data);
        System.out.println("查询支付状态--------------------------------------------------------"+resp.get("trade_state_desc"));
//        System.out.println("查询结果："+JSONObject.toJSONString(resp));
        return resp;
    }


}
