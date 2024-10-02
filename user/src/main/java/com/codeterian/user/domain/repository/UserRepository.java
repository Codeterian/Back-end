package com.codeterian.user.domain.repository;

import com.codeterian.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
