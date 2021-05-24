package com.ape.common.utils;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/23
 */
public class CommonUtil {
    private static final String TELEPHONE_REGEX = "^[1][34578]\\d{9}$";

    public static boolean TelephoneRegex(String telephone){
        Pattern pattern = Pattern.compile(TELEPHONE_REGEX);
        return pattern.matcher(telephone).matches();
    }
}
