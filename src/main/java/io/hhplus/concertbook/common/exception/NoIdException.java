package io.hhplus.concertbook.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoIdException extends Exception {
    public NoIdException() {
        super();
    }

    public NoIdException(String message) {
        super(message);
    }
}
