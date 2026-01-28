package com.nm.novamart.Controller;

import com.nm.novamart.Dto.Authorization.*;
import com.nm.novamart.Service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("api/user")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> addUser(@RequestBody RegisterRequestDto registerDto) {
        UserResponseDto user = userService.addUser(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(authRequestDto));
    }

    @PostMapping("/isTokenExpired")
    public ResponseEntity<?> isTokenExpired(@RequestBody TokenRequestDto tokenRequestDto) {
        Boolean response = userService.isTokenExpired(tokenRequestDto.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
