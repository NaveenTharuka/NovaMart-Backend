package com.nm.novamart.Service;

import com.nm.novamart.Dto.Authorization.AuthRequestDto;
import com.nm.novamart.Dto.Authorization.AuthResponseDto;
import com.nm.novamart.Dto.CartItems.CartItemResponseDto;
import com.nm.novamart.Dto.Authorization.RegisterRequestDto;
import com.nm.novamart.Dto.Authorization.UserResponseDto;
import com.nm.novamart.Entity.User;
import com.nm.novamart.Exeptions.DuplicateEmailException;
import com.nm.novamart.Mapper.UserMapper;
import com.nm.novamart.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CartServiceImpl cartService;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;

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

    public AuthResponseDto login(AuthRequestDto loginRequest) throws AuthenticationException {

        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if(!(authentication.isAuthenticated())){
            throw  new AuthenticationException("Invalid username and password");
        }

        User user = userRepository.getUserByEmail(loginRequest.getEmail());

        return AuthResponseDto.builder()
                .token(jwtService.generateToken(loginRequest.getEmail()))
                .role(user.getRole())
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();
    }

    public Boolean isTokenExpired(String token) {
        if(token == null) {
            return false;
        }
        return jwtService.isTokenExpired(token);
    }

}
