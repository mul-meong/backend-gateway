package com.mulmeong.gateway.common.jwt.util;

import com.mulmeong.gateway.common.exception.BaseException;
import com.mulmeong.gateway.common.jwt.properties.JwtProperties;
import com.mulmeong.gateway.common.response.BaseResponseStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Slf4j
@Service
public class JwtProvider {

    private final Environment env;
    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    /**
     * 토큰 검증 : 파싱시 예외가 발생하면 false 반환.
     *
     * @param token jwtToken
     * @return true(유효) / false(X)
     */
    //토큰 유효성 체크 메서드
    public boolean isValidToken(String token) {
        try {
            parseClaims(token.trim());
            return true;
        } catch (Exception e) {
            logAndThrow("토큰이 유효하지 않습니다", e);
            return false;
        }
    }

    /**
     * 토큰 파싱 : 예외 발생시 로그 + 예외 처리.
     *
     * @param token jwtToken
     */
    private void parseClaims(String token) {
        try {
            if (token == null) {
                throw new BaseException(BaseResponseStatus.NO_JWT_TOKEN);
            }
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            logAndThrow("만료된 토큰입니다", e);
        } catch (UnsupportedJwtException e) {
            logAndThrow("지원되지 않는 유형의 토큰입니다", e);
        } catch (MalformedJwtException | IllegalArgumentException e) {
            logAndThrow("잘못된 토큰입니다", e);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logAndThrow("SecretKey가 일치하지 않습니다", e);
        } catch (Exception e) {
            logAndThrow("토큰이 유효하지 않습니다", e);
        }
    }

    // 로그 + 예외
    private void logAndThrow(String message, Exception e) {
        log.error(message, e);
        throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
    }

    // SecretKey 생성
    public SecretKey getSecretKey() {
        if (secretKey == null) {
            String secret = jwtProperties.getSecretKey();
            if (secret == null) {
                throw new RuntimeException("SecretKey 환경변수가 설정되지 않았습니다.");
            }
            secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        }
        return secretKey;
    }
}