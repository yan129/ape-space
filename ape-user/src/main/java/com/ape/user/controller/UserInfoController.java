package com.ape.user.controller;

import com.ape.common.model.ResultVO;
import com.ape.user.feign.OssServiceFeign;
import com.ape.user.model.UserDO;
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

    @ApiOperation(value = "修改昵称", notes = "修改昵称")
    @PostMapping("/modifyNickname/{uid}")
    public ResultVO modifyNickname(@PathVariable("uid") String uid, @RequestParam("nickname") String nickname){
        UserDO userDO = new UserDO();
        userDO.setId(uid);
        userDO.setNickname(nickname);
        boolean result = userService.updateById(userDO);
        return result ? ResultVO.OK() : ResultVO.ERROR();
    }

    @ApiOperation(value = "修改个性签名", notes = "修改个性签名")
    @PostMapping("/modifyRemark/{uid}")
    public ResultVO modifyRemark(@PathVariable("uid") String uid, @RequestParam("remark") String remark){
        UserDO userDO = new UserDO();
        userDO.setId(uid);
        userDO.setNickname(remark);
        boolean result = userService.updateById(userDO);
        return result ? ResultVO.OK() : ResultVO.ERROR();
    }

    @ApiOperation(value = "修改主题颜色", notes = "修改主题颜色")
    @PostMapping("/modifyThemeColor/{uid}")
    public ResultVO modifyThemeColor(@PathVariable("uid") String uid, @RequestParam("themeColor") String themeColor){
        UserDO userDO = new UserDO();
        userDO.setId(uid);
        userDO.setThemeColor(themeColor);
        boolean result = userService.updateById(userDO);
        return result ? ResultVO.OK() : ResultVO.ERROR();
    }

}
