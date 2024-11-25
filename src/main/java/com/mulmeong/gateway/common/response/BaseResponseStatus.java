package com.mulmeong.gateway.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {
    /*
     * 응답 코드와 메시지 표준화하는 ENUM.
     * Http 상태코드, 성공 여부, 응답 메시지, 커스텀 응답 코드, 데이터를 반환.
     */

    /**
     * 200: 요청 성공.
     **/
    SUCCESS(HttpStatus.OK, true, 200, "요청에 성공하였습니다."),

    // 600 : JWT 토큰 에러
    WRONG_JWT_TOKEN(HttpStatus.UNAUTHORIZED, false, 600, "다시 로그인 해주세요"),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, false, 601, "토큰이 만료되었습니다."),
    NO_JWT_TOKEN(HttpStatus.UNAUTHORIZED, false, 602, "토큰이 없습니다."),
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, false, 603, "토큰이 유효하지 않습니다."),

    // 900: 기타 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 900, "요청 처리 중 에러가 발생하였습니다.(Gateway)");


    private final HttpStatusCode httpStatusCode;
    private final boolean isSuccess;
    private final int code;
    private final String message;

}
