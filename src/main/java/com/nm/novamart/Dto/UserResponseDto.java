package com.nm.novamart.Dto;

import com.nm.novamart.Dto.CartItems.CartItemResponseDto;
import com.nm.novamart.Entity.CartItems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponseDto {
    private String userName;
    private String email;
    private Long cartId;
    private List<CartItemResponseDto> cartItems;

}
