//package com.mulmeong.gateway.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    /**
//     * WebMVC CORS 설정을 위한 메서드로, 특정 URL 패턴에 대해 CORS 규칙을 정의
//     * 모든 출처와 모든 HTTP 메서드에 대해 접근을 허용하도록 설정.
//     *
//     * @param registry CORS 매핑 설정을 위한 CorsRegistry 객체
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*");
//    }
//}
