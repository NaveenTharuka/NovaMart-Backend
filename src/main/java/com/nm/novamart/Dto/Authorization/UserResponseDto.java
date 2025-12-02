package com.nm.novamart.Dto.Authorization;

import com.nm.novamart.Dto.CartItems.CartItemResponseDto;
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
