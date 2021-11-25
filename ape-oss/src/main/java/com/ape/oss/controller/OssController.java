package com.ape.oss.controller;

import com.ape.common.model.ResultVO;
import com.ape.oss.service.OssService;
import io.swagger.annotations.Api;
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

    @PostMapping("/upload")
    public ResultVO<Map<String, Object>> upload(MultipartFile file, String folder, Boolean imgSuffixCheckSwitch, Boolean datePathStorageSwitch){
        Map<String, Object> map = ossService.uploadFile(file, folder, imgSuffixCheckSwitch, datePathStorageSwitch);
        return ResultVO.OK(map);
    }

    @DeleteMapping("/delete")
    public ResultVO delete(String url){
        ossService.deleteFile(url);
        return ResultVO.OK();
    }
}
