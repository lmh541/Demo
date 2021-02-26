package kr.co.divus.home.util;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvier {
    @Value("spring.jwt.secret")
    private String SECRET_KEY;

    private long tokenValidMilisecond = 1000L * 60 * 60; // 1시간만 토큰 유효

    @PostConstruct
    protected void init() {
    	SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    // Jwt 토큰 생성
    public String createToken(String userPk) {
      Claims claims = Jwts.claims().setSubject(userPk);
      Date now = new Date();
      return Jwts.builder()
            .setClaims(claims) // 데이터
            .setIssuedAt(now) // 토큰 발행일자
            .setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 암호화 알고리즘, secret값 세팅
            .compact();
    }
}
