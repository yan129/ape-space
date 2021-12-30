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
 *
 * success：返回是否成功
 * code：返回状态码
 * message：描述消息
 * encrypt：用于判断数据是否加密，true加密，false不加密
 * data：返回数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResultVO<T> implements Serializable {

    private Boolean success;
    private Integer code;
    private String message;
    private Boolean encrypt;
    private T data;

    public ResultVO(Boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.encrypt = false;
    }

    public static <T> ResultVO<T> OK(){
        return new ResultVO<>(Boolean.TRUE, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), null);
    }

    public static <T> ResultVO<T> OK(T data){
        if (data instanceof ResponseCode){
            return new ResultVO<>(Boolean.TRUE, ((ResponseCode) data).getCode(), ((ResponseCode) data).getMsg(), null);
        }
        return new ResultVO<>(Boolean.TRUE, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), data);
    }

    public static <T> ResultVO<T> ERROR(){
        return new ResultVO<>(Boolean.FALSE, ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMsg(), null);
    }

    public static <T> ResultVO<T> ERROR(T data){
        if (data instanceof ResponseCode){
            return new ResultVO<>(Boolean.FALSE, ((ResponseCode) data).getCode(), ((ResponseCode) data).getMsg(), null);
        }
        return new ResultVO<>(Boolean.FALSE, ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMsg(), data);
    }
}
