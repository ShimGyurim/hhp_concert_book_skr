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
        bookRepository.deleteAll();
        concertItemRepository.deleteAll();
        concertRepository.deleteAll();
        paymentRepository.deleteAll();
        seatRepository.deleteAll();
        userRepository.deleteAll();
        waitTokenRepository.deleteAll();
        walletRepository.deleteAll();
    }
}
