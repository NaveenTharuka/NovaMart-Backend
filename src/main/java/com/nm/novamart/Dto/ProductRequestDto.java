package com.nm.novamart.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    private String name;
    private String description;
    private double price;
    private int quantity;
    private String category;

//    private String ImgURL;

}
