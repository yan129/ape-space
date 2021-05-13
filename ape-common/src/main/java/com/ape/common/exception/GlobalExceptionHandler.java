package com.ape.common.exception;

import com.ape.common.model.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/13
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResultVO handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = bindingResult.getFieldError().getDefaultMessage();
        log.info("参数校验失败：{}", errorMsg);
        return ResultVO.ERROR().setMessage(errorMsg);
    }

    @ExceptionHandler(ServiceException.class)
    public ResultVO handleCustomException(ServiceException e){
        log.error("异常原因：{}", e.getMsg());
        return ResultVO.ERROR().setMessage(e.getMsg());
    }

}
