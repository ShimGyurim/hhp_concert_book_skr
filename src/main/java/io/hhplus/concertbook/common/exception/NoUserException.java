package io.hhplus.concertbook.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoUserException extends Exception {
    public NoUserException() {
        super();
    }

    public NoUserException(String message) {
        super(message);
    }
}
