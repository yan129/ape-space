package com.ape.user.feign;

import com.ape.common.model.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/12/4
 */
@Component
@FeignClient(value = "OSS-SERVICE")
public interface OssServiceFeign {

    /**
     * 上传文件接口
     * @param file 上传的文件
     * @param folder 要保存的文件夹路径，开头不用加 /
     * @param imgSuffixCheckSwitch 是否打开文件后缀校验，仅限图片后缀，true开启，false关闭
     * @param datePathStorageSwitch 是否打开文件存储时，自动按日期格式生成文件夹来保存文件，true开启，false关闭
     * @return 返回上传文件的成功标识和访问路径
     */
    @PostMapping(value = "/oss/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultVO<String> upload(@RequestPart("file") MultipartFile file, @RequestParam("folder") String folder,
                            @RequestParam("imgSuffixCheckSwitch") Boolean imgSuffixCheckSwitch, @RequestParam("datePathStorageSwitch") Boolean datePathStorageSwitch);

}
