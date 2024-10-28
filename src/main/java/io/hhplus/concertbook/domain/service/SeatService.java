package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.entity.SeatEntity;
import io.hhplus.concertbook.domain.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {
    @Autowired
    SeatRepository seatRepository;

    public SeatEntity findAndLockSeat(long seatId) throws CustomException {
        SeatEntity seat = seatRepository.findByIdWithLock(seatId);
        if (seat == null) {
            throw new CustomException(ErrorCode.SEAT_ERROR);
        }
        if (seat.isUse()) {
            throw new CustomException(ErrorCode.SEAT_FULL);
        }
        seat.setUse(true);
        seatRepository.save(seat);
        return seat;
    }
}
