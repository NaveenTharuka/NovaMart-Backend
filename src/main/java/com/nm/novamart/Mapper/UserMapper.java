package com.nm.novamart.Mapper;

import com.nm.novamart.Dto.Authorization.RegisterRequestDto;
import com.nm.novamart.Dto.Authorization.UserResponseDto;
import com.nm.novamart.Entity.Cart;
import com.nm.novamart.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User toEntity(RegisterRequestDto requestDto) {
        if (requestDto.getEmail() == null || requestDto.getPassword() == null || requestDto.getUserName() == null) {
            return null;
        }
        Cart newCart = new Cart();

        return User.builder()
                .userName(requestDto.getUserName())
                .password(encoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .address(requestDto.getAddress())
                .role(requestDto.getRole())
                .cart(newCart)
                .build();
    }

    public UserResponseDto toResponse(User user) {

        return UserResponseDto.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .cartId(user.getCart().getId())
                .build();
    }
}
