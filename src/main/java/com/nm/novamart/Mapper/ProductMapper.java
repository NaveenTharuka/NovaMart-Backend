package com.nm.novamart.Mapper;

import com.nm.novamart.Dto.ProductRequestDto;
import com.nm.novamart.Dto.ProductResponseDto;
import com.nm.novamart.Dto.ProductUpdateReqDto;
import com.nm.novamart.Entity.Category;
import com.nm.novamart.Entity.Product;
import com.nm.novamart.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryRepository categoryRepository;

    public Product toProduct(ProductRequestDto productReqDto) {
        if(productReqDto == null) {
            return null;
        }
        if(productReqDto.getCategory() == null || !(categoryRepository.existsByName(productReqDto.getCategory()))) {
            return null;
        }

        Category category = categoryRepository.getCategoryByName(productReqDto.getCategory());

        return Product.builder()
                .name(productReqDto.getName())
                .description(productReqDto.getDescription())
                .price(productReqDto.getPrice())
                .quantity(productReqDto.getQuantity())
                .category(category)
                .build();
    }

    public ProductResponseDto toResponse(Product product) {
        if(product == null) {
            return null;
        }

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory().getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    public Product updateProduct(Product product,ProductUpdateReqDto productUpdateReqDto) {

        if(productUpdateReqDto == null ||  product == null) {
            return null;
        }
        product.setName(productUpdateReqDto.getName());
        product.setDescription(productUpdateReqDto.getDescription());
        product.setPrice(productUpdateReqDto.getPrice());
        product.setQuantity(productUpdateReqDto.getQuantity());

        Category category = categoryRepository.getCategoryByName(productUpdateReqDto.getCategory());

        product.setCategory(category);

        return product;
    }
}
