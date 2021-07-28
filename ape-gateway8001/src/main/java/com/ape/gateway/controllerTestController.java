package com.ape.gateway;

import com.ape.gateway.constant.AuthConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/27
 */
@RestController
public class controllerTestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("test")
    public void test(){
        Map<String, String> map = new HashMap<>();
        map.put("name", "lisi");
        redisTemplate.opsForHash().putAll("aa", map);
        System.out.println(redisTemplate.opsForHash().entries("aa"));
        System.out.println(redisTemplate.opsForHash().entries(AuthConstant.RESOURCE_ROLES_KEY));
    }
}
