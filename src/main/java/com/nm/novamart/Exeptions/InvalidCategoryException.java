package com.nm.novamart.Exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidCategoryException extends BaseException {
    public InvalidCategoryException(String category) {
        super("Invalid Category Type : " + category);
    }
}
