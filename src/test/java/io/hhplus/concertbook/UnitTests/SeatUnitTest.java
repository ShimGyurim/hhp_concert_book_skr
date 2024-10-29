package io.hhplus.concertbook.UnitTests;

import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.entity.SeatEntity;
import io.hhplus.concertbook.domain.repository.SeatRepository;
import io.hhplus.concertbook.domain.service.SeatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class SeatUnitTest {
    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("좌석 정보 (락 객체) : 성공")
    public void testFindAndLockSeat_Success() throws CustomException {
        long seatId = 1L;
        SeatEntity seat = new SeatEntity();
        seat.setUse(false);

        Mockito.when(seatRepository.findByIdWithLock(seatId)).thenReturn(seat);

        SeatEntity result = seatService.findAndLockSeat(seatId);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isUse());
        Mockito.verify(seatRepository).findByIdWithLock(seatId);
        Mockito.verify(seatRepository).save(seat);
    }

    @Test
    @DisplayName("좌석 정보 (락 객체) : 좌석정보 못찾음")
    public void testFindAndLockSeat_SeatNotFound() {
        long seatId = 1L;

        Mockito.when(seatRepository.findByIdWithLock(seatId)).thenReturn(null);

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            seatService.findAndLockSeat(seatId);
        });

        Assertions.assertEquals(ErrorCode.SEAT_ERROR, exception.getErrorCode());
    }
}
