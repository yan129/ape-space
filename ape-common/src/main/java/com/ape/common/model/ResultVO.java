package com.ape.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/13
 * 统一封装实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResultVO<T> implements Serializable {

    private Boolean success;
    private Integer code;
    private String message;
    private T data;

    public static <T> ResultVO<T> OK(){
        return new ResultVO<>(Boolean.TRUE, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), null);
    }

    public static <T> ResultVO<T> OK(T data){
        return new ResultVO<>(Boolean.TRUE, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), data);
    }

    public static <T> ResultVO<T> ERROR(){
        return new ResultVO<>(Boolean.FALSE, ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMsg(), null);
    }

    public static <T> ResultVO<T> ERROR(T data){
        return new ResultVO<>(Boolean.FALSE, ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMsg(), data);
    }
}
