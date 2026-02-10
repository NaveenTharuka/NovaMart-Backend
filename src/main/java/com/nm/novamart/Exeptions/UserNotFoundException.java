package com.nm.novamart.Exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends BaseException {
    public UserNotFoundException(UUID userId) {
        super("User not found with id :" + userId);
    }

    public UserNotFoundException(String username) {
        super("User not found with username :" + username);
    }
}
