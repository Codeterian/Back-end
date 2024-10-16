package com.codeterian.queue.presentation.controller;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.entity.UserRole;
import com.codeterian.common.infrastructure.util.CurrentPassport;
import com.codeterian.common.infrastructure.util.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/queues")
@RequiredArgsConstructor
public class QueueController {


    //사용자 대기열 진입
    @GetMapping("/join")
    public ResponseEntity<ResponseDto<Void>> joinQueue(@CurrentPassport Passport passport) {

        //서비스 로직

        return ResponseEntity.ok(ResponseDto.ok());
    }

    //필요 시 관리자 수동 주입
    @GetMapping("/next")
    public ResponseEntity<ResponseDto<Void>> nextInQueue(@CurrentPassport Passport passport)
            throws IllegalAccessException {

        if(passport.getUserRole()== UserRole.CUSTOMER) {
            throw new IllegalAccessException();
        }

        //서비스 로직

        return ResponseEntity.ok(ResponseDto.ok());
    }
}
