package com.ape.user.feign;

import com.ape.common.model.ResultVO;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/18
 */

@Component
@FeignClient(value = "APE-SMS")
public interface SmsServiceFeign {

    /**
     * 远程发送短信接口
     * @param telephone
     * @return
     *
     * 远程调用SMS服务发送短信的方法，默认等待1秒，得不到报错
     */
    @PostMapping("/sms/send/{telephone}")
    ResultVO<String> send(@ApiParam("手机号码") @PathVariable("telephone") String telephone);
}
