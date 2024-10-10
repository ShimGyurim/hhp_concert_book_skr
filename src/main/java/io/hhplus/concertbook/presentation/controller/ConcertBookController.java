package io.hhplus.concertbook.presentation.controller;

import io.hhplus.concertbook.presentation.HttpDto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcertBookController {

    @GetMapping("/default")
    public String getMethod() {
        return "success";
    }

    @PostMapping("/auth/token")
    @Operation(summary = "토큰발급", description = "토큰을 사용자에게 발급")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<CommonResponse<Object>> issueUserToken() {
        String token = "1";
        CommonResponse<Object> response = CommonResponse.builder()
                .msg("토큰 발급 성공")
                .data(token)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/concerts/date")
    public void getAvailableDates(@RequestBody
                                              @Parameter(required = true, description = "토큰입력")
                                                      String token) {

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("")
//                .data()
                .build();

//        return ResponseEntity.status(HttpStatus.OK).body(response);
        return ;
    }

    @PostMapping("/concerts/seat")
    public void getAvailableSeats(@RequestBody
                                          @Parameter(required = true, description = "토큰입력")
                                                  String token) {

        CommonResponse<Object> response = CommonResponse.builder()
                .msg("")
//                .data()
                .build();

//        return ResponseEntity.status(HttpStatus.OK).body(response);
        return ;
    }


    @PostMapping("/reservations")
    @Operation(summary = "좌석예약", description = "콘서트 예약 API")
    public void requestSeatReservation() {
        //토큰, 날짜, 좌석, 콘서트id 모두 필요
    }

    @PostMapping("/users/recharge")
    public void rechargeBalance( ) {
        // 사용자 , 금액 
    }

    @PostMapping("/users/balance")
    public void getBalance( ) {
        // 사용자
    }

    @PostMapping("/payments")
    public void makePayment( ) {

    }


}