package io.hhplus.concertbook.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    String name;
    String message;

    public ErrorResponse(String message) {
        this.message=message;
    }
}
