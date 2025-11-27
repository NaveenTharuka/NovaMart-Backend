package com.nm.novamart.Dto;

import com.nm.novamart.Enum.Role;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponseDto {

    private String token;
    private String userName;
    private String email;
    private Role role;


}
