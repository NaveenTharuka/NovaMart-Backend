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

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public AuthResponseDto login(AuthRequestDto loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            if (!authentication.isAuthenticated()) {
                throw new RuntimeException("Invalid email or password");
            }

            // Fetch user from DB
            User user = userRepository.getUserByEmail(loginRequest.getEmail());
            if (user == null) {
                throw new RuntimeException("User not found with this email");
            }

            String token = jwtService.generateToken(loginRequest.getEmail());

            // Return token + user info
            return AuthResponseDto.builder()
                    .token(token)
                    .role(user.getRole())
                    .id(user.getId())
                    .userName(user.getUserName())
                    .email(user.getEmail())
                    .build();

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        } catch (InternalAuthenticationServiceException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found with this email");
        } catch (RuntimeException e) {
            // any other runtime error
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }



    public Boolean isTokenExpired(String token) {
        if(token == null) {
            return false;
        }
        return jwtService.isTokenExpired(token);
    }

}
