package io.hhplus.concertbook.presentation.HttpDto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonResponse<T> {

    private String msg;
    private T data;

    @Builder
    public CommonResponse(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }
}
