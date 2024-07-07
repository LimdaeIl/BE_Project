package com.fastcampus.aptner.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class HttpResponse<T> {
    private final Integer code; // 1성공, 실패
    private final String message;
    private final T data;
}
