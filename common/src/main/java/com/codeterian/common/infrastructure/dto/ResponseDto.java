package com.codeterian.common.infrastructure.dto;

import com.codeterian.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    private final int code;

    private final T data;

    private final String message;

    private final LocalDateTime responseAt;

    @Builder
    private ResponseDto(int code, String message, T data, LocalDateTime responseAt) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.responseAt = responseAt;
    }

    public static ResponseDto<Void> ok() {
        return ResponseDto.<Void>builder()
                .code(HttpStatus.OK.value())
                .data(null)
                .message(null)
                .responseAt(LocalDateTime.now())
                .build();
    }

    public static <T> ResponseDto<T> okWithData(T data) {
        return ResponseDto.<T>builder()
                .code(HttpStatus.OK.value())
                .data(data)
                .message(null)
                .responseAt(LocalDateTime.now())
                .build();
    }
}
