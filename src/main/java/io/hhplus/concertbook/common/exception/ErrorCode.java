package io.hhplus.concertbook.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_AUTH("권한없음", "권한없음.", HttpConstant.FORBIDDEN),
    USER_ERROR("사용자", "사용자가 없거나 무효.", HttpConstant.INTERNAL_SERVER_ERROR),
    TOKEN_ERROR("토큰무효", "토큰이 없거나 무효.", HttpConstant.INTERNAL_SERVER_ERROR),
    TOKEN_EXPIRED("토큰만료", "토큰이 만료.", HttpConstant.INTERNAL_SERVER_ERROR),
    TOKEN_WAIT("토큰대기", "토큰대기.", HttpConstant.INTERNAL_SERVER_ERROR),
    SEAT_ERROR("좌석무효", "좌석정보가 없거나 무효", HttpConstant.INTERNAL_SERVER_ERROR),
    SEAT_FULL("좌석사용중", "좌석사용중", HttpConstant.INTERNAL_SERVER_ERROR),
    CHARGE_INPUT_ERROR("잔액입력이상", "잔액입력이상", HttpConstant.INTERNAL_SERVER_ERROR),
    BOOK_ERROR("예약무효", "예약무효.", HttpConstant.INTERNAL_SERVER_ERROR),
    NO_API_INFO("API지정정보없음", "API지정정보없음.", HttpConstant.INTERNAL_SERVER_ERROR),
    NO_USERINFO("사용자정보없음", "사용자정보없음.", HttpConstant.INTERNAL_SERVER_ERROR),
    NO_PAY("결제대상없음", "결제대상없음.", HttpConstant.INTERNAL_SERVER_ERROR),
    NO_CONCERT("콘서트없음", "콘서트없음.", HttpConstant.INTERNAL_SERVER_ERROR),
    NO_WALLET("잔액정보없음", "잔액정보없음.", HttpConstant.INTERNAL_SERVER_ERROR),
    NO_BALANCE("잔액없음", "잔액없음.", HttpConstant.INTERNAL_SERVER_ERROR),
    MONEY_ERROR("잔액무효", "잔액정보 무효.", HttpConstant.INTERNAL_SERVER_ERROR);


    private final String name;
    private final String msg;
    private final int code;
}
