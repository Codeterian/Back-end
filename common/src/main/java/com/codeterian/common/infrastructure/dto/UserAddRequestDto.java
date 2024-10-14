package com.codeterian.common.infrastructure.dto;

import com.codeterian.common.infrastructure.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserAddRequestDto(
        @NotBlank(message = "name은 필수값입니다.")
        String name,
        @NotBlank(message = "password은 필수값입니다.")
        @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하여야합니다.")
        String password,
        @NotBlank(message = "email은 필수값입니다.")
        @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[A-Za-z0-9][A-Za-z0-9-]*(\\.[A-Za-z]{2,})+$", message = "이메일 형식에 맞게 입력해주세요")
        String email,

        @NotNull(message = "userRole은 필수값입니다.")
        UserRole userRole
) {

}
