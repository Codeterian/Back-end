package com.codeterian.common.infrastructure.dto;

import com.codeterian.common.infrastructure.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {

    private final int code;

    private final T data;

    private final String message;

    private final LocalDateTime responseAt;

    @Builder
    private ResponseDTO(int code, String message, T data, LocalDateTime responseAt) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.responseAt = responseAt;
    }

    public static ResponseDTO<Void> ok() {
        return ResponseDTO.<Void>builder()
                .code(HttpStatus.OK.value())
                .data(null)
                .message(null)
                .responseAt(LocalDateTime.now())
                .build();
    }

    public static <T> ResponseDTO<T> okWithData(T data) {
        return ResponseDTO.<T>builder()
                .code(HttpStatus.OK.value())
                .data(data)
                .message(null)
                .responseAt(LocalDateTime.now())
                .build();
    }

    public static <T> ResponseDTO<T> okWithDataAndMessage(String message, T data) {
        return ResponseDTO.<T>builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .responseAt(LocalDateTime.now())
                .build();
    }

    public static ResponseDTO<Void> error(ErrorCode errorCode) {
        return ResponseDTO.<Void>builder()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .data(null)
                .responseAt(LocalDateTime.now())
                .build();
    }

    public static <T> ResponseDTO<T> errorWithMessage(HttpStatus httpStatus, String errorMessage) {
        return ResponseDTO.<T>builder()
                .code(httpStatus.value())
                .message(errorMessage)
                .data(null)
                .responseAt(LocalDateTime.now())
                .build();
    }

}
