package com.ape.user.controller;

import com.ape.common.model.ResultVO;
import com.ape.user.feign.OssServiceFeign;
import com.ape.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/12/4
 */
@Api(value = "用户信息控制器", description = "用户信息控制器")
@Slf4j
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private UserService userService;
    @Autowired
    private OssServiceFeign ossServiceFeign;

    @ApiOperation(value = "修改头像", notes = "修改头像")
    @PostMapping("/modifyAvatar/{uid}")
    public ResultVO modifyAvatar(@RequestPart("file") MultipartFile file, @PathVariable("uid") String uid){
        ResultVO<String> uploadInfo = ossServiceFeign.upload(file, "avatar", true, false);

        if (!uploadInfo.getSuccess()) {
            return ResultVO.ERROR().setMessage(uploadInfo.getData());
        }

        boolean update = userService.modifyAvatar(uid, uploadInfo.getData());
        return update ? ResultVO.OK(uploadInfo.getData()) : ResultVO.ERROR().setMessage("更换失败");
    }
}
