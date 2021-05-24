package com.ape.sms.controller;

import com.ape.common.model.ResultVO;
import com.ape.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "发送短信", notes = "发送短信")
    @PostMapping("/{telephone}")
    public ResultVO<String> send(@ApiParam("手机号码") @PathVariable("telephone") String telephone){

        return ResultVO.OK();
    }

    @ApiOperation(value = "发送短信", notes = "发送短信")
    @PostMapping("/hello")
    public ResultVO<String> hello(){

        return ResultVO.OK();
    }

}
