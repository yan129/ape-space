package com.ape.oauth2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/1
 */
@RestController
public class HelloController {

    @GetMapping("/ape/hello")
    public String hello() {
        return "hello success !!!";
    }

    @GetMapping("/ape/test")
    public String test() {
        return "test success !!!";
    }

    @GetMapping("/ape/user")
    public String user() {
        return "user success !!!";
    }

    @GetMapping("/ape/common")
    public String common() {
        return "common success !!!";
    }
}
