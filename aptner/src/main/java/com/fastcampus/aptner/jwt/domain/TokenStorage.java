//package com.fastcampus.aptner.jwt.domain;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.data.annotation.CreatedDate;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@ToString
//@Table(name = "tokenStorage")
//@Entity
//public class TokenStorage {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "tokenStorage_id", nullable = false, updatable = false)
//    private Long tokenStorageId; // 토큰 id
//
//    @Column(length = 512)
//    private String refreshToken; // 리프레시 토큰 번호
//
//    @CreatedDate
//    private LocalDateTime createdAt; // 생성일자
//
//    private Long memberId; // 회원 id
//
//    @Builder
//    public TokenStorage(String refreshToken, Long memberId) {
//        this.refreshToken = refreshToken;
//        this.memberId = memberId;
//        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
//    }
//}
