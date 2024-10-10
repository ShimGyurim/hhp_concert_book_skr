package io.hhplus.concertbook.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DateParameterException extends Exception {
    public DateParameterException() {
        super();
    }

    public DateParameterException(String message) {
        super(message);
    }
}
