package com.nm.novamart.Dto.OrderDto;

import com.nm.novamart.Dto.CartItems.CartItemRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutOrderReqDto {

        private UUID userId;
        private List<CartItemRequestDto> cartItems;
        private String address;
        private String comment;

}
