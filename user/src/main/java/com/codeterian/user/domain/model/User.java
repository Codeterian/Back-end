package com.codeterian.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String password;

    @Column
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
}
