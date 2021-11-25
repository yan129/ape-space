package com.ape.oss.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ape.common.utils.StringUtils;
import com.ape.oss.constant.OssConstant;
import com.ape.oss.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/11/25
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

    private static final String[] IMAGE_TYPE = {".jpg", ".jpeg", ".gif", ".png"};
    private static final String DOT = ".";
    private static final String DATE_PATTERN = "yyyy/MM/dd";

    @Override
    public Map<String, Object> uploadFile(MultipartFile file, String folder, Boolean imgSuffixCheckSwitch, Boolean datePathStorageSwitch) {
        OSS ossClient = new OSSClientBuilder().build(OssConstant.END_POINT, OssConstant.ACCESSKEY_ID, OssConstant.ACCESSKEY_SECRET);

        Map<String, Object> resultMap = new HashMap<>();

        if (imgSuffixCheckSwitch){
            boolean existsSuffix = this.imgSuffixCheck(file);
            if (!existsSuffix){
                resultMap.put("status", false);
                resultMap.put("message", "图片格式必须为：'.jpg','.jpeg','.gif','.png'");
                return resultMap;
            }
        }

        if (!ossClient.doesBucketExist(OssConstant.BUCKET_NAME)){
            ossClient.createBucket(OssConstant.BUCKET_NAME);
        }

        String datePath = null;
        if (datePathStorageSwitch){
            datePath = DateUtil.format(new Date(), DATE_PATTERN);
        }

        try {
            InputStream is = file.getInputStream();
            String oldFilename = file.getOriginalFilename();
            oldFilename = IdUtil.simpleUUID() + DOT + StringUtils.substringAfterLast(oldFilename, DOT);

            String newFilename;
            if (StringUtils.isNotBlank(datePath)){
                newFilename = folder + File.separator + datePath + File.separator + oldFilename;
            }else {
                newFilename = folder + File.separator + oldFilename;
            }

            ossClient.putObject(OssConstant.BUCKET_NAME, newFilename, is);
            ossClient.shutdown();

            String url = "https://" + OssConstant.BUCKET_NAME + DOT + OssConstant.END_POINT + File.separator + newFilename;

            resultMap.put("status", true);
            resultMap.put("message", url);

            return resultMap;
        } catch (IOException e) {
            log.error(e.getMessage());
            resultMap.put("status", false);
            resultMap.put("message", "未知错误!");
            return resultMap;
        }

    }

    private boolean imgSuffixCheck(MultipartFile file) {
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)){
                isLegal = true;
                break;
            }
        }
        return isLegal;
    }

    @Override
    public void downloadFile() {

    }

    @Override
    public void deleteFile(String url) {
        if (StringUtils.isBlank(url)){
            return;
        }

        OSS ossClient = new OSSClientBuilder().build(OssConstant.END_POINT, OssConstant.ACCESSKEY_ID, OssConstant.ACCESSKEY_SECRET);

        ossClient.deleteObject(OssConstant.BUCKET_NAME, url);

        ossClient.shutdown();
    }
}
