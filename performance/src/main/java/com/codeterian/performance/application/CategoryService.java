package com.codeterian.performance.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeterian.performance.domain.category.Category;
import com.codeterian.performance.infrastructure.persistence.CategoryRepositoryImpl;
import com.codeterian.performance.presentation.dto.request.CategoryModifyRequestDto;
import com.codeterian.performance.presentation.dto.request.ChildCategoryAddRequestDto;
import com.codeterian.performance.presentation.dto.request.ParentCategoryAddRequestDto;
import com.codeterian.performance.presentation.dto.response.CategoryDetailsResponseDto;
import com.codeterian.performance.presentation.dto.response.CategoryModifyResponseDto;
import com.codeterian.performance.presentation.dto.response.ChildCategoryAddResponseDto;
import com.codeterian.performance.presentation.dto.response.ParentCategoryAddResponseDto;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepositoryImpl categoryRepository;

    public ParentCategoryAddResponseDto addParentCategory(ParentCategoryAddRequestDto dto) {
        if (categoryRepository.existsByNameAndIsDeletedFalse(dto.name())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }

        Category newCategory = Category.builder()
                .name(dto.name())
                .build();

        Category saveCategory = categoryRepository.save(newCategory);
        return ParentCategoryAddResponseDto.fromEntity(saveCategory);
    }

    public ChildCategoryAddResponseDto addChildCategory(ChildCategoryAddRequestDto dto) {
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

        Category saveCategory = categoryRepository.save(newCategory);
        return ChildCategoryAddResponseDto.fromEntity(saveCategory);
    }

    @Transactional
    public CategoryModifyResponseDto modifyCategory(UUID categoryId, CategoryModifyRequestDto dto) {
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
        if (dto.parentId() != null) {
            // 기존 parent가 null이 아닌 경우 비교, null이면 바로 업데이트
            if (category.getParent() == null || !category.getParent().getId().equals(dto.parentId())) {
                if (!categoryRepository.existsById(dto.parentId())) {
                    throw new IllegalArgumentException("상위 카테고리가 존재하지 않습니다.");
                }
                category.modifyParentId(dto.parentId());
            }
        }

        Category saveCategory = categoryRepository.save(category);
        return CategoryModifyResponseDto.valueOf(saveCategory);
    }

    @Transactional
    public void removeCategory(UUID categoryId) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );

        // 나중에 userId 받아와서 수정하기
        category.delete(1L);

        categoryRepository.save(category);
    }

    public CategoryDetailsResponseDto findCategoryDetails(UUID categoryId) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );


        if (category.getParent() == null) {
            return new CategoryDetailsResponseDto(category.getName(),"부모 카테고리 입니다.");
        }

        return new CategoryDetailsResponseDto(category.getName(), category.getParent().getName());
    }
}
