package com.nm.novamart.Controller;

import com.nm.novamart.Dto.AuthRequestDto;
import com.nm.novamart.Dto.AuthResponseDto;
import com.nm.novamart.Dto.RegisterRequestDto;
import com.nm.novamart.Dto.UserResponseDto;
import com.nm.novamart.Entity.User;
import com.nm.novamart.Service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> addUser(@RequestBody RegisterRequestDto registerDto) {
        UserResponseDto user = userService.addUser(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) throws AuthenticationException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.login(authRequestDto));
    }

}
