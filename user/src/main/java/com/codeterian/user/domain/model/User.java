package com.codeterian.user.domain.model;

import com.codeterian.common.infrastructure.entity.BaseEntity;
import com.codeterian.common.infrastructure.entity.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;

    // 팩토리 메서드를 통해 객체 생성
    public static User create(String name, String password, String email, UserRole role) {
        return new User(name, password, email, role);
    }

    // private 생성자 (팩토리 메서드를 통해서만 객체 생성 가능)
    private User(String name, String password, String email, UserRole userRole) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = userRole;
    }

    public void modifyUserInfo(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
