package com.mulmeong.gateway.common.jwt.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulmeong.gateway.common.jwt.properties.JwtProperties;
import com.mulmeong.gateway.common.jwt.util.JwtProvider;
import com.mulmeong.gateway.common.response.BaseResponse;
import com.mulmeong.gateway.common.response.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * JWT 토큰 인증 필터
 * 헤더의 토큰을 검증하여 유효한지 확인하고, 유효하지 않으면 예외를 반환.
 */
@Slf4j
@Component("JwtAuthenticationFilter")
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, JwtProperties jwtProperties) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
        this.jwtProperties = jwtProperties;
    }

    public static class Config {
        // Put the configuration properties
    }

    /**
     * JWT 토큰 인증 필터, 토큰이 유효한지 확인
     * 유효하지 않으면 예외 반환 {@link #handleException(ServerWebExchange, BaseResponseStatus)}).
     *
     * @param config 필터 설정 정보
     * @return GatewayFilter 객체, 다음 필터체인으로 요청 전달
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 요청에서 헤더를 가져오고, 헤더에 있는 토큰을 가져옴
            String authorizationHeader = exchange.getRequest().getHeaders()
                    .getFirst(jwtProperties.getAccessTokenPrefix());

            if (authorizationHeader == null || !authorizationHeader.startsWith(jwtProperties.getTokenPrefix())) {
                // 헤더에 토큰이 없거나, 토큰 접두사가 아닌 경우 필터를 통과
                log.info("헤더에 토큰이 없어 통과합니다.");
                return handleException(exchange, BaseResponseStatus.NO_JWT_TOKEN);
            }

            // String token = authorizationHeader.substring(jwtProperties.getTokenPrefix().length()).trim();
            if (!jwtProvider.isValidToken(authorizationHeader.substring(
                    jwtProperties.getTokenPrefix().length()).trim())) {
                // 토큰이 유효하지 않은 경우 예외처리
                return handleException(exchange, BaseResponseStatus.TOKEN_NOT_VALID);
            }

            // 토큰이 유효한 경우 다음 필터로 이동
            return chain.filter(exchange);
        };
    }

    /**
     * 응답 객체를 생성하여 예외(401) 응답 반환.
     *
     * @param exchange 현재의 서버 요청-응답 객체
     * @param status   BaseResponseStatus 객체
     * @return Mono of type Void, webflux 비동기 처리 객체
     */
    private Mono<Void> handleException(ServerWebExchange exchange, BaseResponseStatus status) {
        ServerHttpResponse response = exchange.getResponse(); // 서버 응답 객체
        response.setStatusCode(HttpStatus.UNAUTHORIZED); // 상태 코드 설정
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON); // 헤더 설정

        // 응답 객체 생성
        BaseResponse<String> baseResponse = new BaseResponse<>(status);

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] data;
        try {
            data = objectMapper.writeValueAsBytes(baseResponse);
        } catch (JsonProcessingException e) {
            // JSON 변환에 실패할 경우 빈 배열로 초기화
            data = new byte[0];
        }

        // JSON 데이터로 응답을 전송
        DataBuffer buffer = response.bufferFactory().wrap(data);
        return response.writeWith(Mono.just(buffer)).then(Mono.empty());
    }
}
