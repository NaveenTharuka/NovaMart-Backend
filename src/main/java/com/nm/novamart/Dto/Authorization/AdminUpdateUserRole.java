package com.nm.novamart.Dto.Authorization;

import com.nm.novamart.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateUserRole {
    private UUID id;
    private String role;
}
