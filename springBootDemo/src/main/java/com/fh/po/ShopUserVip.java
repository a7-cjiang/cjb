package com.fh.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName("shop_user_vip")
public class ShopUserVip {
 @TableId(value = "id",type = IdType.AUTO)
 private Integer id;
 @TableField("name")
 private String name;
 @DateTimeFormat(pattern="yyyy-MM-dd")
 @TableField("birthday")
 private Date birthday;
 @TableField("district")
 private String district;
 @TableField("img")
 private String img;
 @TableField("number")
 private String number;
 @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
 @TableField("registrationDate")
 private Date registrationDate;

 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public Date getBirthday() {
  return birthday;
 }

 public void setBirthday(Date birthday) {
  this.birthday = birthday;
 }

 public String getDistrict() {
  return district;
 }

 public void setDistrict(String district) {
  this.district = district;
 }

 public String getImg() {
  return img;
 }

 public void setImg(String img) {
  this.img = img;
 }

 public String getNumber() {
  return number;
 }

 public void setNumber(String number) {
  this.number = number;
 }

 public Date getRegistrationDate() {
  return registrationDate;
 }

 public void setRegistrationDate(Date registrationDate) {
  this.registrationDate = registrationDate;
 }
}
