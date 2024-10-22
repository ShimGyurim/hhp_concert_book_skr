package io.hhplus.concertbook.common.exception;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    int code;
    String message;
    String name;

    public ErrorResponse(String name, String message) {
        this.name=name;
        this.message=message;
    }
    public ErrorResponse(String message) {
        this.message=message;
    }
}
