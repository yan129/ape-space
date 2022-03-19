package com.ape.oss.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import com.ape.common.exception.ServiceException;
import com.ape.common.utils.StringUtils;
import com.ape.oss.constant.OssConstant;
import com.ape.oss.service.OssService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
    private static final String SEPARATOR = "/";
    private static final String DOT = ".";
    private static final String DATE_PATTERN = "yyyy/MM/dd";

    @Override
    public Map<String, Object> uploadFile(MultipartFile file, String folder, Boolean imgSuffixCheckSwitch, Boolean datePathStorageSwitch) {
        OSS ossClient = new OSSClientBuilder().build(OssConstant.END_POINT, OssConstant.ACCESSKEY_ID, OssConstant.ACCESSKEY_SECRET);

        Map<String, Object> resultMap = new HashMap<>();

        if (imgSuffixCheckSwitch){
            boolean existsSuffix = this.imgSuffixCheck(file.getOriginalFilename());
            if (!existsSuffix){
                resultMap.put("status", false);
                resultMap.put("message", "图片格式必须为：'.jpg','.jpeg','.gif','.png'");
                return resultMap;
            }
        }

        if (!ossClient.doesBucketExist(OssConstant.BUCKET_NAME)){
            CreateBucketRequest bucketRequest = this.createBucketRequest();
            ossClient.createBucket(bucketRequest);
        }

        String datePath = this.generateDatePath(datePathStorageSwitch);

        try {
            InputStream is = file.getInputStream();
            String oldFilename = file.getOriginalFilename();
            oldFilename = IdUtil.simpleUUID() + DOT + StringUtils.substringAfterLast(oldFilename, DOT);

            String newFilename;
            if (StringUtils.isNotBlank(datePath)){
                newFilename = folder + SEPARATOR + datePath + SEPARATOR + oldFilename;
            }else {
                newFilename = folder + SEPARATOR + oldFilename;
            }

            ossClient.putObject(OssConstant.BUCKET_NAME, newFilename, is);
            ossClient.shutdown();

            String url = this.createRequestUrl(oldFilename, newFilename);

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

    private boolean imgSuffixCheck(String filename) {
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(filename, type)){
                isLegal = true;
                break;
            }
        }
        return isLegal;
    }

    private CreateBucketRequest createBucketRequest(){
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(OssConstant.BUCKET_NAME);
        createBucketRequest.setStorageClass(StorageClass.Standard);
        createBucketRequest.setDataRedundancyType(DataRedundancyType.LRS);
        createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
        return createBucketRequest;
    }

    private String generateDatePath(Boolean dateSwitch){
        String datePath = null;
        if (dateSwitch){
            datePath = DateUtil.format(new Date(), DATE_PATTERN);
        }
        return datePath;
    }

    private String createRequestUrl(String filename, String newFilename){
        boolean isImg = this.imgSuffixCheck(filename);
        String url = newFilename;
        if (isImg || StringUtils.endsWithIgnoreCase(filename, ".pdf")){
            url = "https://" + OssConstant.BUCKET_NAME + DOT + OssConstant.END_POINT + SEPARATOR + newFilename;
        }
        return url;
    }

    @Override
    public Map<String, Object> breakPointUploadFile(MultipartFile file, String folder, Boolean datePathStorageSwitch) {
        OSS ossClient = new OSSClientBuilder().build(OssConstant.END_POINT, OssConstant.ACCESSKEY_ID, OssConstant.ACCESSKEY_SECRET);

        Map<String, Object> resultMap = new HashMap<>();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setCacheControl("no-cache");
        metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        metadata.setContentEncoding("UTF-8");
        metadata.setContentType(file.getContentType());

        String datePath = this.generateDatePath(datePathStorageSwitch);

        String oldFilename = file.getOriginalFilename();
        String newFilename = folder + SEPARATOR + datePath + SEPARATOR + IdUtil.simpleUUID() + DOT + StringUtils.substringAfterLast(oldFilename, DOT);

        CommonsMultipartFile cmf = (CommonsMultipartFile) file;
        DiskFileItem diskFileItem = (DiskFileItem) cmf.getFileItem();
        final File localFile = diskFileItem.getStoreLocation();

        UploadFileRequest uploadFileRequest = new UploadFileRequest(OssConstant.BUCKET_NAME, newFilename);
        uploadFileRequest.setUploadFile(localFile.getAbsolutePath());
        uploadFileRequest.setPartSize(1 * 1024 * 1024L);
        uploadFileRequest.setEnableCheckpoint(true);
        uploadFileRequest.setCheckpointFile("checkpoint");
        uploadFileRequest.setObjectMetadata(metadata);
        try {
            ossClient.uploadFile(uploadFileRequest);
            ossClient.shutdown();

            String url = this.createRequestUrl(oldFilename, newFilename);

            resultMap.put("status", true);
            resultMap.put("message", url);

            return resultMap;
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
            resultMap.put("status", false);
            resultMap.put("message", "未知错误!");
            return resultMap;
        }
    }

    private void multipartUpload(MultipartFile file){
        OSS ossClient = new OSSClientBuilder().build(OssConstant.END_POINT, OssConstant.ACCESSKEY_ID, OssConstant.ACCESSKEY_SECRET);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setCacheControl("no-cache");
        metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        metadata.setContentEncoding("UTF-8");
        metadata.setContentType(file.getContentType());

        String oldFilename = file.getOriginalFilename();
        String newFilename = IdUtil.simpleUUID() + DOT + StringUtils.substringAfterLast(oldFilename, DOT);

//        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(OssConstant.BUCKET_NAME, newFilename);
//        request.setObjectMetadata(metadata);
//
//        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
//        String uploadId = upresult.getUploadId();
//        List<PartETag> partETags =  new ArrayList<>();
//        final long partSize = 1 * 1024 * 1024L;

        CommonsMultipartFile cmf = (CommonsMultipartFile) file;
        DiskFileItem diskFileItem = (DiskFileItem) cmf.getFileItem();
        final File storeLocation = diskFileItem.getStoreLocation();
//        long fileLength = storeLocation.length();
//
//        int partCount = (int) (fileLength / partSize);
//        if (fileLength % partSize != 0) {
//            partCount++;
//        }
//
//        // 遍历分片上传。
//        for (int i = 0; i < partCount; i++) {
//            long startPos = i * partSize;
//            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
//            InputStream instream = new FileInputStream(storeLocation);
//            // 跳过已经上传的分片。
//            instream.skip(startPos);
//            UploadPartRequest uploadPartRequest = new UploadPartRequest();
//            uploadPartRequest.setBucketName(OssConstant.BUCKET_NAME);
//            uploadPartRequest.setKey(newFilename);
//            uploadPartRequest.setUploadId(uploadId);
//            uploadPartRequest.setInputStream(instream);
//            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
//            uploadPartRequest.setPartSize(curPartSize);
//            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
//            uploadPartRequest.setPartNumber( i + 1);
//            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
//            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
//            // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
//            partETags.add(uploadPartResult.getPartETag());
//        }
//
//        CompleteMultipartUploadRequest completeMultipartUploadRequest =
//                new CompleteMultipartUploadRequest(OssConstant.BUCKET_NAME, newFilename, uploadId, partETags);
//
//        // 完成上传。
//        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
//        System.out.println(completeMultipartUploadResult.getETag());
//        // 关闭OSSClient。
//        ossClient.shutdown();
    }

    @Override
    public void downloadFile() {

    }

    @Override
    public Boolean deleteFile(String filePath) {
        if (StringUtils.isBlank(filePath)){
            return false;
        }

        OSS ossClient = new OSSClientBuilder().build(OssConstant.END_POINT, OssConstant.ACCESSKEY_ID, OssConstant.ACCESSKEY_SECRET);

        filePath = this.substringThirdIndexSlash(filePath);

        if (!ossClient.doesObjectExist(OssConstant.BUCKET_NAME, filePath)) {
            throw new ServiceException("文件不存在");
        }

        ossClient.deleteObject(OssConstant.BUCKET_NAME, filePath);

        if (ossClient != null) {
            ossClient.shutdown();
        }
        return true;
    }

    private String substringThirdIndexSlash(String url) {
        int thirdIndex = 0;
        for (int i = 0; i < 3; i++) {
            thirdIndex = url.indexOf("/", thirdIndex);
            thirdIndex++;
        }
        return StringUtils.substring(url, thirdIndex);
    }
}
