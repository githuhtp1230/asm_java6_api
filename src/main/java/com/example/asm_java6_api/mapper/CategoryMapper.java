package com.example.asm_java6_api.mapper;

import org.mapstruct.Mapper;

import com.example.asm_java6_api.dto.response.category.CategoryResponse;
import com.example.asm_java6_api.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);
}
