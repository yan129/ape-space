package com.ape.common.utils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;

import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/23
 */
public class CommonUtil {
    private static final String TELEPHONE_REGEX = "^[1][34578]\\d{9}$";

    public static boolean telephoneRegex(String telephone){
        Pattern pattern = Pattern.compile(TELEPHONE_REGEX);
        return pattern.matcher(telephone).matches();
    }

    public static Map<String, Object> encapsulateRequestArgs(ServletRequest request){
        Map<String, Object> argsMap = new HashMap<>(8);
        Enumeration<String> names = request.getParameterNames();

        while (names.hasMoreElements()){
            String elementName = names.nextElement();
            String[] values = request.getParameterValues(elementName);
            if (ArrayUtil.isNotEmpty(values)){
                if (values.length == 1){
                    argsMap.put(elementName, values[0]);
                }else {
                    argsMap.put(elementName, ListUtil.toList(values));
                }
            }
        }

        return argsMap;
    }

}
