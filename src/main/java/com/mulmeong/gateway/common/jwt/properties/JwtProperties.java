package com.mulmeong.gateway.common.jwt.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    //자바 클래스에 jwt 프로퍼티값을 가져와 사용할 수 있게 함

    private String issuer;
    private String secretKey;
    private long accessExpireTime;
    private long refreshExpireTime;
    private String tokenPrefix;
    private String accessTokenPrefix;
    private String refreshTokenPrefix;

}

