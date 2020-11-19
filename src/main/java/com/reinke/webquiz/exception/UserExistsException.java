package com.reinke.webquiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserExistsException extends RuntimeException{

    public UserExistsException(String email) {
        super(String.format("User with email: @s already exists.", email));
    }

}
