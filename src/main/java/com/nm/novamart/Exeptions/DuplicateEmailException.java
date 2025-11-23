package com.nm.novamart.Exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailException extends BaseException {
    public DuplicateEmailException(String email) {

        super("Email is already in use :  " + email);
    }
}
