package com.codeterian.user.application.service;

import com.codeterian.user.domain.model.User;
import com.codeterian.user.domain.repository.UserRepository;
import com.codeterian.user.presentation.dto.request.UserAddRequestDto;
import com.codeterian.user.presentation.dto.request.UserModifyRequestDto;
import com.codeterian.user.presentation.dto.response.UserFindResponseDto;
import com.codeterian.user.presentation.dto.response.UserModifyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserFindResponseDto> findAllUser() {
        return userRepository.findAll().stream()
                .map(UserFindResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public UserModifyResponseDto modifyUser(Long userId, UserModifyRequestDto requestDto) {

        User user = userRepository.findById(userId).orElseThrow(
                //Global Exception Handler
        );

        user.modifyUserInfo(requestDto.name(), requestDto.email());

        return UserModifyResponseDto.fromEntity(user);
    }

//    public void deleteUser(Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(
//                //Global Exception Handler
//        );
//        user.delete(user.getId())
//    }

}
