package com.ape.common.utils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/3
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static boolean isEmpty(Object val) {
        if (val == null) {
            return true;
        } else {
            String valStr = val.toString();
            return "".equals(valStr);
        }
    }

    public static boolean isNotEmpty(Object val) {
        return !isEmpty(val);
    }
}
