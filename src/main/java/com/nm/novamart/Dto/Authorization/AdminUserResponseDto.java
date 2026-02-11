package com.nm.novamart.Dto.Authorization;

import com.nm.novamart.Dto.CartItems.CartItemResponseDto;
import com.nm.novamart.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdminUserResponseDto {
    private UUID id;
    private String userName;
    private String email;
    private Long cartId;
    private List<CartItemResponseDto> cartItems;
    private Role role;
    private Long phoneNumber;
    private int orderCount;
    private String address;
}
