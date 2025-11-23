package com.nm.novamart.Dto;

import jakarta.persistence.Column;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}
