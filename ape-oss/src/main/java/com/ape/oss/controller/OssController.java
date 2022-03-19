package com.ape.oss.controller;

import com.ape.common.model.ResultVO;
import com.ape.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/11/25
 */
@Api(value = "文件控制器", description = "文件控制器")
@Slf4j
@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    private OssService ossService;

    @ApiOperation(value = "文件上传", notes = "文件上传")
    @PostMapping("/upload")
    public ResultVO<String> upload(MultipartFile file, String folder, Boolean imgSuffixCheckSwitch, Boolean datePathStorageSwitch){
        Map<String, Object> map = ossService.uploadFile(file, folder, imgSuffixCheckSwitch, datePathStorageSwitch);
        return ((boolean) map.get("status")) ? ResultVO.OK(((String) map.get("message"))) : ResultVO.ERROR(((String) map.get("message")));
    }

    @ApiOperation(value = "文件断点续传", notes = "文件断点续传")
    @PostMapping("/breakpointUpload")
    public ResultVO<Object> breakpointUpload(MultipartFile file, String folder, Boolean datePathStorageSwitch){
        Map<String, Object> map = ossService.breakPointUploadFile(file, folder, datePathStorageSwitch);
        return ResultVO.OK(map);
    }

    @ApiOperation(value = "删除文件", notes = "删除文件")
    @DeleteMapping("/delete")
    public ResultVO delete(String url){
        Boolean isDelete = ossService.deleteFile(url);
        return isDelete ? ResultVO.OK() : ResultVO.ERROR();
    }
}
