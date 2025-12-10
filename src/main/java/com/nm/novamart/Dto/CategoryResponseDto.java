package com.nm.novamart.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryResponseDto {
    private Long id;
    private String name;
}
