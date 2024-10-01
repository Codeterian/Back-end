package com.codeterian.user.application.service;

import com.codeterian.user.domain.model.User;
import com.codeterian.user.domain.repository.UserRepository;
import com.codeterian.user.presentation.dto.request.UserAddRequestDto;
import com.codeterian.user.presentation.dto.response.UserFindResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void addUser(UserAddRequestDto requestDto) {
        User user = User.create(requestDto.name(), requestDto.password(), requestDto.name());

        userRepository.save(user);
    }

    public UserFindResponseDto findUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                //Global Exception Handler
        );

        return UserFindResponseDto.fromEntity(user);
    }

}
