package com.nm.novamart.Service;

import com.nm.novamart.Dto.CartItems.CartItemResponseDto;
import com.nm.novamart.Dto.RegisterRequestDto;
import com.nm.novamart.Dto.UserResponseDto;
import com.nm.novamart.Entity.User;
import com.nm.novamart.Exeptions.DuplicateEmailException;
import com.nm.novamart.Mapper.CartItemMapper;
import com.nm.novamart.Mapper.UserMapper;
import com.nm.novamart.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CartServiceImpl cartService;

    public UserResponseDto addUser(RegisterRequestDto requestDto) {


        if(userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateEmailException("Email is already in use :  " + requestDto.getEmail());
        }

        User user = userMapper.toEntity(requestDto);
        userRepository.save(user);

        UserResponseDto responseDto = userMapper.toResponse(user);

        List<CartItemResponseDto> cartItems = cartService.getCartItems(user.getId());
        responseDto.setCartItems(cartItems);


        return responseDto;
    }

}
