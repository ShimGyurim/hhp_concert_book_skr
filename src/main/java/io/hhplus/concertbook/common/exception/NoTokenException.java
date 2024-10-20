package io.hhplus.concertbook.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoTokenException extends Exception {
    public NoTokenException() {
        super();
    }

    public NoTokenException(String message) {
        super(message);
    }
}
