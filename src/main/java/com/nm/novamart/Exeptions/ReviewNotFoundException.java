package com.nm.novamart.Exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReviewNotFoundException extends BaseException {
    public ReviewNotFoundException(String message) {
        super(message);
    }

    public ReviewNotFoundException(UUID id) {
        super(String.format("Review with id %s not found", id));
    }
}
