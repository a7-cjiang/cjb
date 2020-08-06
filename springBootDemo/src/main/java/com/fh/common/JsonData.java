package com.fh.common;

public class JsonData {

    private Integer code;

    private String message;

    private Object data;


    private JsonData(Object data){
        this.data=data;
        this.code=200;
        this.message="success";
    }

    private JsonData(String message){
        this.code=500;
        this.message=message;
    }

    private JsonData(Integer code, String message){
        this.code=code;
        this.message=message;
    }




    public static JsonData success(Object data){
        return new JsonData(data);
    }

    public static JsonData success(){
        return new JsonData(200);
    }

    public static JsonData error(String message){
        return new JsonData(message);
    }

    public static JsonData error(Integer code, String message){ return new JsonData(code,message); }






    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String missage) {
        this.message = missage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
