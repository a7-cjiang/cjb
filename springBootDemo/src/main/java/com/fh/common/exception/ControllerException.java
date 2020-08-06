package com.fh.common.exception;

import com.fh.common.JsonData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//springmvc的全局异常处理
@ControllerAdvice
public class ControllerException {


    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public JsonData arithmeticException(){
        return JsonData.error("算术异常");
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public JsonData nullPointerException(){
        return JsonData.error("空指针异常");
    }


    @ExceptionHandler(NologinException.class) //
    @ResponseBody
    public JsonData handleNoLoginException(NologinException e){
        return JsonData.error(1000,e.getMessage());
    }



    /**
     * 处理所有不可知异常
     *
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonData handleException(Exception e) {

        return JsonData.error(e.getMessage());
    }





public static void main(String[] args){

}

}
