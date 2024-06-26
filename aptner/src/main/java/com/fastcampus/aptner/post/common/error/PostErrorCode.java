package com.fastcampus.aptner.post.common.error;

import com.fastcampus.aptner.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {
    NOT_SAME_USER(HttpStatus.FORBIDDEN, "The author and the user are not the same"),
    NOT_ALLOWED_APARTMENT(HttpStatus.FORBIDDEN, "The apartment access is not permitted"),
    CANT_DELETE(HttpStatus.BAD_REQUEST, "Cant delete because it contains post"),
    NO_SUCH_POST(HttpStatus.NOT_FOUND,"The post matching the given ID could not be found"),
    NO_SUCH_CATEGORY(HttpStatus.NOT_FOUND,"The Category matching the given ID could not be found"),
    COMMENT_CONVERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"error when converting comment");

    private final HttpStatus httpStatus;
    private final String message;
}
