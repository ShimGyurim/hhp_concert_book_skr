package io.hhplus.concertbook.application.facade;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.service.*;
import io.hhplus.concertbook.event.Pay.PayEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PayFacade {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ConcertService concertService;
    @Autowired
    private UserService userService;
    @Autowired
    private MoneyService moneyService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BookService bookService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public boolean pay(String token, Long bookId) throws Exception {
        WaitTokenEntity waitToken = tokenService.validateToken(token, ApiNo.PAYMENT);
        BookEntity book = concertService.findAndLockBook(bookId);
        UserEntity userBook = book.getUser();
        UserEntity userToken = waitToken.getUser();
        userService.validateUser(userBook, userToken);

        SeatEntity seat = book.getSeat();
        if (seat == null) {
            throw new CustomException(ErrorCode.SEAT_ERROR);
        }
        ConcertEntity concert = seat.getConcertItem().getConcert();
        if (concert == null) {
            throw new CustomException(ErrorCode.NO_CONCERT);
        }
        long fee = concert.getFee();

        WalletEntity wallet = moneyService.findAndLockWallet(userBook.getUserId());
        moneyService.deductAmount(wallet, fee);

        PaymentEntity payment = paymentService.createPayment(book);
        bookService.updateBookStatus(book, BookStatus.PAID);

        eventPublisher.publishEvent(new PayEvent(payment, waitToken));

        return true;
    }
}
