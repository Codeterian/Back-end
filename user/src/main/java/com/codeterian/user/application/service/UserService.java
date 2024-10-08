package com.codeterian.user.application.service;

import com.codeterian.common.infrastructure.entity.UserRole;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.user.domain.model.User;
import com.codeterian.user.domain.repository.UserRepository;
import com.codeterian.common.infrastructure.dto.UserAddRequestDto;
import com.codeterian.user.presentation.dto.request.UserModifyRequestDto;
import com.codeterian.user.presentation.dto.response.UserFindAllInfoResponseDto;
import com.codeterian.user.presentation.dto.response.UserFindResponseDto;
import com.codeterian.user.presentation.dto.response.UserModifyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입



    @Transactional
    public void addUser(UserAddRequestDto requestDto) {

        String encodedPassword = passwordEncoder.encode(requestDto.password());

        User user = User.create(requestDto.name(), encodedPassword, requestDto.email(), requestDto.userRole());

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserFindResponseDto findUserById(Long userId, Passport passport) throws IllegalAccessException {

        UserRole userRole = passport.getUserRole();
        if (userRole == UserRole.CUSTOMER) {
            throw new IllegalAccessException();
        }

        User user = userRepository.findById(userId).orElseThrow(
                //Global Exception Handler
        );

        return UserFindResponseDto.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public UserFindAllInfoResponseDto findByEmail(String email) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email).orElseThrow(
                    //global Exception Handler
        );

        return UserFindAllInfoResponseDto.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public List<UserFindResponseDto> findAllUser(Passport passport) throws IllegalAccessException {

        UserRole userRole = passport.getUserRole();
        if (userRole == UserRole.CUSTOMER) {
            throw new IllegalAccessException();
        }

        return userRepository.findAll().stream()
                .map(UserFindResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserModifyResponseDto modifyUser(Long userId, UserModifyRequestDto requestDto, Passport passport) throws IllegalAccessException {

        UserRole userRole = passport.getUserRole();
        if (userRole == UserRole.CUSTOMER) {
            throw new IllegalAccessException();
        }

        User user = userRepository.findById(userId).orElseThrow(
                //Global Exception Handler
        );

        user.modifyUserInfo(requestDto.name(), requestDto.email());

        return UserModifyResponseDto.fromEntity(user);
    }

    @Transactional
    public void deleteUser(Long userId, Passport passport) throws IllegalAccessException {

        UserRole userRole = passport.getUserRole();
        if (userRole == UserRole.CUSTOMER) {
            throw new IllegalAccessException();
        }

        User user = userRepository.findById(userId).orElseThrow(
                //Global Exception Handler
        );
        user.delete(user.getId());
    }

}
