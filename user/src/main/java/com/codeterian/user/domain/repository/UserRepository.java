package com.codeterian.user.domain.repository;

import com.codeterian.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    Optional<User> findByNameAndDeletedAtIsNull(String name);

}
