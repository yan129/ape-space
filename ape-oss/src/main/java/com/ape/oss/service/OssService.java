package com.ape.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/11/25
 */
public interface OssService {


    /**
     * 上传文件
     * @param file 上传的文件
     * @param folder 要保存的文件夹路径
     * @param imgSuffixCheckSwitch 是否打开文件后缀校验，仅限图片后缀，true开启，false关闭
     * @param datePathStorageSwitch 是否打开文件存储时，自动按日期格式生成文件夹来保存文件，true开启，false关闭
     * @return 返回上传文件的成功标识和访问路径
     */
    Map<String, Object> uploadFile(MultipartFile file, String folder, Boolean imgSuffixCheckSwitch, Boolean datePathStorageSwitch);

    void downloadFile();

    /**
     * 断点续传文件
     * @param file 上传的文件
     * @param folder 要保存的文件夹路径
     * @param datePathStorageSwitch 是否打开文件存储时，自动按日期格式生成文件夹来保存文件，true开启，false关闭
     * @return 返回上传文件的成功标识和访问路径
     */
    Map<String, Object> breakPointUploadFile(MultipartFile file, String folder, Boolean datePathStorageSwitch);

    /**
     * 删除文件
     * @param filePath 删除文件的路径
     */
    void deleteFile(String filePath);
}
