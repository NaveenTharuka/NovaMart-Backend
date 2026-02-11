package com.nm.novamart.Controller;

import com.nm.novamart.Dto.CategoryResponseDto;
import com.nm.novamart.Service.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;


    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponseDto>> findAll() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories().getBody();

        return ResponseEntity.status(HttpStatus.OK).body(categories);

    }
}
