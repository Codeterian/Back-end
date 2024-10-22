package com.codeterian.performance.application;

import static com.codeterian.performance.infrastructure.exception.CategoryErrorCode.*;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeterian.common.exception.RestApiException;
import com.codeterian.common.infrastructure.entity.UserRole;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.performance.domain.category.Category;
import com.codeterian.performance.infrastructure.exception.CategoryErrorCode;
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

    public ParentCategoryAddResponseDto addParentCategory(ParentCategoryAddRequestDto dto, Passport passport) throws IllegalAccessException{

        validateAdminRole(passport);

        if (categoryRepository.existsByNameAndIsDeletedFalse(dto.name())) {
            throw new RestApiException(CONFLICT_DUPLICATE_CATEGORY);
        }

        Category newCategory = Category.addParentCategory(dto,passport.getUserId());

        Category saveCategory = categoryRepository.save(newCategory);
        return ParentCategoryAddResponseDto.fromEntity(saveCategory);
    }

    public ChildCategoryAddResponseDto addChildCategory(ChildCategoryAddRequestDto dto, Passport passport) {

        validateAdminRole(passport);

        Category parentCategory = categoryRepository.findByIdAndIsDeletedFalse(dto.parentId()).orElseThrow(
                () -> new RestApiException(NOT_FOUND_PARENT_CATEGORY)
        );

        // 중복 카테고리명 확인 추가
        if (categoryRepository.existsByNameAndIsDeletedFalse(dto.name())) {
            throw new RestApiException(CONFLICT_DUPLICATE_CATEGORY);
        }

        Category newCategory = Category.addChildCategory(dto, parentCategory,passport.getUserId());

        Category saveCategory = categoryRepository.save(newCategory);
        return ChildCategoryAddResponseDto.fromEntity(saveCategory);
    }

    @Transactional
    public CategoryModifyResponseDto modifyCategory(UUID categoryId, CategoryModifyRequestDto dto, Passport passport) {

        validateAdminRole(passport);

        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(
                ()-> new RestApiException(NOT_FOUND_CATEGORY)
        );

        // 카테고리명 중복 확인 && 카테고리명 업데이트
        if (!category.getName().equals(dto.name()) && dto.name() != null) {
            if (categoryRepository.existsByNameAndIsDeletedFalse(dto.name())) {
                throw new RestApiException(CONFLICT_DUPLICATE_CATEGORY);
            }
            category.modifyCategoryName(dto.name());
        }

        // 부모 카테고리 존재 유무 확인 && 부모 카테고리 업데이트
        if (dto.parentId() != null) {
            // 기존 parent가 null이 아닌 경우 비교, null이면 바로 업데이트
            if (category.getParent() == null || !category.getParent().getId().equals(dto.parentId())) {
                if (!categoryRepository.existsById(dto.parentId())) {
                    throw new RestApiException(NOT_FOUND_PARENT_CATEGORY);
                }
                category.modifyParentId(dto.parentId());
            }
        }

        Category saveCategory = categoryRepository.save(category);
        return CategoryModifyResponseDto.valueOf(saveCategory);
    }

    @Transactional
    public void removeCategory(UUID categoryId, Passport passport) {

        validateAdminRole(passport);

        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(
                () -> new RestApiException(NOT_FOUND_CATEGORY)
        );

        category.delete(passport.getUserId());

        categoryRepository.save(category);
    }

    public CategoryDetailsResponseDto findCategoryDetails(UUID categoryId, Passport passport) {

        validateAdminRole(passport);

        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(
                () -> new RestApiException(NOT_FOUND_CATEGORY)
        );


        if (category.getParent() == null) {
            return new CategoryDetailsResponseDto(category.getName(),"부모 카테고리 입니다.");
        }

        return new CategoryDetailsResponseDto(category.getName(), category.getParent().getName());
    }

    private static void validateAdminRole(Passport passport) {
        UserRole userRole = passport.getUserRole();

        if (userRole == UserRole.CUSTOMER){
            throw new RestApiException(FORBIDDEN_ADMIN_ACCESS);
        }
    }
}
