package com.ape.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/30
 *
 * 获取请求接口的IP地址
 */
@Slf4j
public class IpUtil {

    private static final String IP_UTILS_FLAG = ",";
    private static final String UNKNOWN = "unknown";
    /**
     * IPV6
     */
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    /**
     * IPV4
     */
    private static final String LOCALHOST_IPV4 = "127.0.0.1";

    /**
     * 获取IP地址
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request){
        String ip = null;
        try {
            //以下两个获取在k8s中，将真实的客户端IP，放到了x-Original-Forwarded-For。而将WAF的回源地址放到了 x-Forwarded-For了。
            ip = request.getHeader("X-Original-Forwarded-For");
            if (StringUtils.isBlank(ip) ||UNKNOWN.equalsIgnoreCase(ip)){
                ip = request.getHeader("X-Forwarded-For");
            }

            //获取Nginx等代理的ip
            if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
                ip = request.getHeader("x-forwarded-for");
            }
            if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }

            //兼容k8s集群获取ip
            if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
                ip = request.getRemoteAddr();
                if (LOCALHOST_IPV4.equalsIgnoreCase(ip) || LOCALHOST_IPV6.equalsIgnoreCase(ip)){
                    //根据网卡取本机配置的IP
                    InetAddress iNet = null;
                    try {
                        iNet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        log.error("GetClientIp Error: {}", e.getMessage());
                    }
                    ip = iNet.getHostAddress();
                }
            }

        }catch (Exception e){
            log.error("IpUtil Error: {}", e.getMessage());
        }

        //使用代理，则获取第一个IP地址
        if (StringUtils.isNotBlank(ip) || ip.indexOf(IP_UTILS_FLAG) > 0){
            if (StringUtils.contains(ip, ",")){
                ip = ip.substring(0, ip.indexOf(IP_UTILS_FLAG));
            }
        }
        return ip;
    }

}
