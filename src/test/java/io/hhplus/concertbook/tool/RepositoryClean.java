package io.hhplus.concertbook.tool;

import io.hhplus.concertbook.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryClean {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ConcertItemRepository concertItemRepository;
    @Autowired
    ConcertRepository concertRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WaitTokenRepository waitTokenRepository;
    @Autowired
    WalletRepository walletRepository;

    public void cleanRepository() {
        // XXX : 제약조건으로 삭제 에러가 나는 경우가 있어 우선 미사용 처리
    }
}
