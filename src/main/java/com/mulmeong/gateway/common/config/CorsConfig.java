package com.mulmeong.gateway.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    /**
     * CORS 설정을 위한 CorsFilter 빈을 생성하여 반환하는 메서드
     * 개별 서비스의 특정 경로에 대해 CORS 설정이 필요한 경우 사용.
     * CORS 설정을 통해 특정 출처의 요청만 허용하거나,
     * 모든 출처에 대해 접근을 허용하도록 구성합니다.
     *
     * @return CORS 설정이 적용된 CorsFilter 인스턴스
     */
    public CorsFilter corsWebFilter() {


        CorsConfiguration config = new CorsConfiguration();

        // 자격 증명 포함을 허용하는 설정 (예: 쿠키, 인증 헤더 등)
        config.setAllowCredentials(true);

        // 모든 출처에 대해 허용(임시)
        // config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOriginPattern("*");

        // 모든 헤더에 대해 허용
        config.addAllowedHeader("*");

        // 모든 메서드에 대해 허용
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // CORS 설정을 모든 경로에 등록
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
