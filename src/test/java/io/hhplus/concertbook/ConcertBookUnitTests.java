package io.hhplus.concertbook;

import io.hhplus.concertbook.common.exception.DateParameterException;
import io.hhplus.concertbook.common.exception.NoIdException;
import io.hhplus.concertbook.presentation.HttpDto.request.*;
import io.hhplus.concertbook.presentation.HttpDto.response.CommonResponse;
import io.hhplus.concertbook.presentation.controller.ConcertBookController;
import io.hhplus.concertbook.presentation.controller.TokenController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ConcertBookUnitTests {

    @InjectMocks
    ConcertBookController concertBookController;

    @Test
    @DisplayName("가능한 날짜 조회 성공 테스트")
    public void getAvailableDates_success() throws Exception {
        // given
        ConcertDateReqDto reqDto = new ConcertDateReqDto("userId", "20240930");

        // when
        ResponseEntity<CommonResponse<Object>> response = concertBookController.getAvailableDates(reqDto);

        // then
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @DisplayName("가능한 날짜 조회 실패 테스트 : 날짜 부정확")
    public void getAvailableDates_fail() throws Exception {
        // given
        ConcertDateReqDto reqDto = new ConcertDateReqDto("userId", "20300000");

        // when, then
        Assertions.assertThrows(DateParameterException.class, () -> concertBookController.getAvailableDates(reqDto));
    }

    @Test
    @DisplayName("가능한 좌석 조회 실패 테스트")
    public void getAvailableSeats_fail_nullToken() throws Exception {
        // given
        ConcertSeatReqDto reqDto = new ConcertSeatReqDto("userId", null);

        // when, then
        Assertions.assertThrows(NoIdException.class, () -> concertBookController.getAvailableSeats(reqDto));
    }

    @Test
    @DisplayName("예약 실패 테스트")
    public void requestSeatReservation_fail_nullConcertScheduleId() throws Exception {
        // given
        ConcertReservReqDto reqDto = new ConcertReservReqDto(null, 50L, "token");

        // when, then
        Assertions.assertThrows(NoIdException.class, () -> concertBookController.requestSeatReservation(reqDto));
    }

    @Test
    @DisplayName("결제 성공 테스트")
    public void makePayment_success() throws Exception {
        // given
        PayReqDto reqDto = new PayReqDto(20L,"token");

        // when
        ResponseEntity<CommonResponse<Object>> response = concertBookController.makePayment(reqDto);

        // then
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @DisplayName("결제 실패 테스트: 예약 id 없음")
    public void makePayment_fail_nullReservationId() throws Exception {
        // given
        PayReqDto reqDto = new PayReqDto(null,"token");

        // when, then
        Assertions.assertThrows(NoIdException.class, () -> concertBookController.makePayment(reqDto));
    }
}
