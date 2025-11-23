package com.nm.novamart.Controller;

import com.nm.novamart.Dto.RegisterRequestDto;
import com.nm.novamart.Dto.UserResponseDto;
import com.nm.novamart.Entity.User;
import com.nm.novamart.Service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
