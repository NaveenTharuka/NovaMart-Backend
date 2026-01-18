package com.nm.novamart.Dto.Authorization;

import com.nm.novamart.Enum.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class AuthResponseDto {

    private String token;
    private String userName;
    private UUID id;
    private String email;
    private Role role;


}
