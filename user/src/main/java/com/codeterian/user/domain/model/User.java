package com.codeterian.user.domain.model;

import com.codeterian.common.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Column
    @Getter
    private String name;

    @Column
    private String password;

    @Column
    @Getter
    private String email;

    // 팩토리 메서드를 통해 객체 생성
    public static User create(String name, String password, String email) {
        return new User(name, password, email);
    }

    // private 생성자 (팩토리 메서드를 통해서만 객체 생성 가능)
    private User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public void modifyUserInfo(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
