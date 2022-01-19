package com.talex.frame.talexframe.handler;

import com.talex.frame.talexframe.exception.FrameException;
import com.talex.frame.talexframe.wrapper.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <br /> {@link com.talex.frame.talexframe.handler Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 10:50 <br /> Project: TalexFrame <br />
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     *
     * 默认全局异常处理
     *
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    public ResultData<Object> exceptionGlobal(Exception exception) {

        if( exception instanceof HttpRequestMethodNotSupportedException ) {

            log.error("[AutomaticAnalyse] NotSupportedRequest: " + exception.getMessage());

            return ResultData.FAILED("NotSupportedRequest: " + ( (HttpRequestMethodNotSupportedException) exception ).getMethod());

        }

        log.error("[GlobalException] Exception: {}", exception.getMessage(), exception);

        return ResultData.FAILED(exception);

    }

    /**
     *
     * 全局框架异常处理
     *
     */
    @ExceptionHandler(FrameException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<FrameException> exceptionGlobal(FrameException exception) {

        log.error("[FrameException] Exception: {}", exception.getMessage(), exception);

        return ResultData.FAILED(exception);

    }

}
