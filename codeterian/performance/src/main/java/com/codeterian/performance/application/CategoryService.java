package com.codeterian.performance.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeterian.performance.domain.category.Category;
import com.codeterian.performance.infrastructure.persistence.CategoryRepositoryImpl;
import com.codeterian.performance.presentation.dto.request.CategoryModifyDto;
import com.codeterian.performance.presentation.dto.request.ChildCategoryAddDto;
import com.codeterian.performance.presentation.dto.request.ParentCategoryAddDto;
import com.codeterian.performance.presentation.dto.response.CategoryDetailsDto;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepositoryImpl categoryRepository;
    private final String username = "test";

    public void addParentCategory(ParentCategoryAddDto dto) {
        if (categoryRepository.existsByNameAndIsDeletedFalse(dto.name())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }

        Category newCategory = Category.builder()
                .name(dto.name())
                .build();

        categoryRepository.save(newCategory);
    }

    public void addChildCategory(ChildCategoryAddDto dto) {
        Category parentCategory = categoryRepository.findByIdAndIsDeletedFalse(dto.parentId()).orElseThrow(
                () -> new IllegalArgumentException("상위 카테고리가 존재하지 않습니다.")
        );

        // 중복 카테고리명 확인 추가
        if (categoryRepository.existsByNameAndIsDeletedFalse(dto.name())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }

        Category newCategory = Category.builder()
                .name(dto.name())
                .parent(parentCategory)
                .build();

        categoryRepository.save(newCategory);
    }

    @Transactional
    public void modifyCategory(UUID categoryId, CategoryModifyDto dto) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );

        // 카테고리명 중복 확인 && 카테고리명 업데이트
        if (!category.getName().equals(dto.name()) && dto.name() != null) {
            if (categoryRepository.existsByNameAndIsDeletedFalse(dto.name())) {
                throw new IllegalArgumentException("중복된 카테고리명입니다.");
            }
            category.modifyCategoryName(dto.name());
        }

        // 부모 카테고리 존재 유무 확인 && 부모 카테고리 업데이트
        if (!category.getParent().getId().equals(dto.parentId()) && dto.parentId() != null) {
            if (!categoryRepository.existsById(dto.parentId())) {
                throw new IllegalArgumentException("상위 카테고리가 존재하지 않습니다.");
            }
            category.modifyParentId(dto.parentId());
        }

        categoryRepository.save(category);
    }

    // @Transactional
    // public void removeCategory(UUID categoryId) {
    //     Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(
    //             () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
    //     );
    //     // // 나중에 userId 받아와서 수정하기
    //     // category.delete(1);
    //     categoryRepository.save(category);
    // }

    public CategoryDetailsDto findCategoryDetails(UUID categoryId) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );


        if (category.getParent() == null) {
            return new CategoryDetailsDto(category.getName(),"부모 카테고리 입니다.");
        }

        return new CategoryDetailsDto(category.getName(), category.getParent().getName());
    }
}
