package com.nm.novamart.Mapper;

import com.nm.novamart.Dto.ProductRequestDto;
import com.nm.novamart.Entity.Product;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static Product toProduct(ProductRequestDto productReqDto) {
        if(productReqDto == null) {
            return null;
        }
        return Product.builder()
                .name(productReqDto.getName())
                .description(productReqDto.getDescription())
                .price(productReqDto.getPrice())
                .quantity(productReqDto.getQuantity())
                .category(productReqDto.getCategory())
                .build();
    }

}
