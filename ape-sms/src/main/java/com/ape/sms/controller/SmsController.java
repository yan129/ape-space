package com.ape.sms.controller;

import com.ape.common.model.ResponseCode;
import com.ape.common.model.ResultVO;
import com.ape.common.utils.CommonUtil;
import com.ape.sms.constant.SmsConstant;
import com.ape.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/24
 */
@Api(value = "短信控制器", description = "短信控制器")
@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "发送短信", notes = "发送短信")
    @PostMapping("/send/{telephone}")
    public ResultVO<String> send(@ApiParam("手机号码") @PathVariable("telephone") String telephone){
        boolean telRegex = CommonUtil.TelephoneRegex(telephone);
        if (!telRegex){
            return ResultVO.ERROR(SmsConstant.PHONE_CORRECT);
        }

        // 判断缓存中key是否过期
        if (stringRedisTemplate.hasKey(SmsConstant.PREFIX + telephone.trim())){
            return ResultVO.ERROR(ResponseCode.REPEAT_OPERATION.getMsg());
        }

        boolean send = smsService.send(telephone);
        return send ? ResultVO.OK(SmsConstant.GET_CODE) : ResultVO.ERROR(SmsConstant.SERVICE_ERROR);
    }

    @ApiOperation(value = "发送短信", notes = "发送短信")
    @GetMapping("/hello")
    public ResultVO<String> hello(){

        return ResultVO.OK();
    }

}
