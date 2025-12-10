package com.nm.novamart.Service;

import com.nm.novamart.Dto.CategoryResponseDto;
import com.nm.novamart.Entity.Category;
import com.nm.novamart.Mapper.CategoryMapper;
import com.nm.novamart.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryResponseDto> categoryResponse = categoryMapper.toResponse(categoryList);
        return ResponseEntity.status(HttpStatus.FOUND).body(categoryResponse);
    }
}
