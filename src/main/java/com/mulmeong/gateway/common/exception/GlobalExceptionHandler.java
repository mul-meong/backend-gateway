package com.mulmeong.gateway.common.exception;

import com.mulmeong.gateway.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Controller 에서 발생한 예외 처리 클래스
     * (Serivce에서 발생한 예외 또한 Controller에서 처리)
     */

    /**
     * 발생한 예외 처리.
     */
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<BaseResponse<String>> handleBaseException(BaseException e) {

        // 예외 메시지를 로그에 기록
        log.error("BaseException 발생 : {}", e.getMessage());

        // 예외 스택을 로그에 기록
        for (StackTraceElement s : e.getStackTrace()) {
            System.out.println(s);
        }

        BaseResponse<String> response = new BaseResponse<>(e.getStatus(), e.getMessage());

        return new ResponseEntity<>(response, response.httpStatus());
    }
}