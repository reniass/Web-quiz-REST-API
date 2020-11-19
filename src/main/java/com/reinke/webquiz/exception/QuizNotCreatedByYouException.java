package com.reinke.webquiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class QuizNotCreatedByYouException extends RuntimeException{

    public QuizNotCreatedByYouException(String message) {
        super(message);
    }
}
