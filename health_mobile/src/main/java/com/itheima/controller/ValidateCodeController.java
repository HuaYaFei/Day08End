package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
//        生成四位验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
//            发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码缓存到redis中
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //用户手机快速登录发送验证码
    @RequestMapping("/send4Login")
//    六位验证码
    public Result send4Login(String telephone) {
        //        生成四位验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        try {
//            发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码缓存到redis中
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5 * 60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
