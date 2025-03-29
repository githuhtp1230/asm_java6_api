package com.example.asm_java6_api.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveCategoryRequest {
    @NotBlank
    @Size(min = 3, max = 20, message = "Name phải nằm trong khoảng 3 - 20 kí tự")
    private String name;
}
