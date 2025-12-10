package com.nm.novamart.Mapper;

import com.nm.novamart.Dto.CategoryResponseDto;
import com.nm.novamart.Entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CategoryMapper {
    public List<CategoryResponseDto> toResponse(List<Category> category) {

        List<CategoryResponseDto> categoryResponseDtos = new ArrayList<>();

        for (Category category1 : category) {
            CategoryResponseDto categoryResponseDto = CategoryResponseDto.builder()
                    .id(category1.getId())
                    .name(category1.getName())
                    .build();
            categoryResponseDtos.add(categoryResponseDto);
        }
        return categoryResponseDtos;
    }
}
