package io.hhplus.concertbook.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.DateTimeException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DateParameterException extends DateTimeException {


    public DateParameterException(String message) {
        super(message);
    }
}
