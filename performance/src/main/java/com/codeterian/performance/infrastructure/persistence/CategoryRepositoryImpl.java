package com.codeterian.performance.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeterian.performance.domain.category.Category;

public interface CategoryRepositoryImpl extends JpaRepository<Category, UUID> {

    Optional<Category> findByIdAndIsDeletedFalse(UUID id);

    Boolean existsByNameAndIsDeletedFalse(String name);

}
