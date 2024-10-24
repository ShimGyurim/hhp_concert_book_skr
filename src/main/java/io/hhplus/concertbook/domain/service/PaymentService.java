package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.BookRepository;
import io.hhplus.concertbook.domain.repository.PaymentRepository;
import io.hhplus.concertbook.domain.repository.WaitTokenRepository;
import io.hhplus.concertbook.domain.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class PaymentService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    WaitTokenRepository waitTokenRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    WaitQueueService waitQueueService;

    @Autowired
    PaymentRepository paymentRepository;

    @Transactional
    public boolean pay(String token, Long bookId) throws Exception {
        if(token == null){
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }
//        waitQueueService.queueRefresh(ApiNo.PAYMENT);

        //TODO: PROCESS 가 service 에 진입한 후 updatedAt 시간 갱신기능
//        Optional<BookEntity> bookEntityOptional = bookRepository.findById(bookId);
        BookEntity book = bookRepository.findByIdWithLock(bookId);
        if(book == null) {
            throw new CustomException(ErrorCode.BOOK_ERROR);
        }
        UserEntity userBook = book.getUser();


        if(!BookStatus.PREPAYMENT.equals(book.getStatusCd())){
            throw new CustomException(ErrorCode.NO_PAY);
        }
        WaitTokenEntity waitToken = waitTokenRepository.findByToken(token);
        if(waitToken == null) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }

        if(WaitStatus.EXPIRED.equals(waitToken.getStatusCd())) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }else if(WaitStatus.WAIT.equals(waitToken.getStatusCd())) {
            throw new CustomException(ErrorCode.TOKEN_WAIT);
        }
        if(!ApiNo.PAYMENT.equals(waitToken.getServiceCd())){
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }

        UserEntity userToken = waitToken.getUser();

        if(userBook == null || userToken == null) {
            throw new CustomException(ErrorCode.USER_ERROR);
        }else if(userBook.getUserId() != userToken.getUserId()) {
            throw new CustomException(ErrorCode.NO_AUTH);
        }
        //결제로직

        //돈있는지 확인
        SeatEntity seat = book.getSeat();
        if(seat == null) {
            throw new CustomException(ErrorCode.SEAT_ERROR);
        }
        ConcertEntity concert = seat.getConcertItem().getConcert();
        if(concert == null) {
            throw new CustomException(ErrorCode.NO_CONCERT);
        }
        long fee = concert.getFee();

        WalletEntity wallet = walletRepository.findByUser_UserIdWithLock(userBook.getUserId());
        if(wallet == null) {
            throw new CustomException(ErrorCode.NO_WALLET);
        }
        if(wallet.getAmount() < fee) {
            throw new CustomException(ErrorCode.NO_BALANCE);
        }

        //지갑 금액 차감
        wallet.setAmount(wallet.getAmount()-fee);
        walletRepository.save(wallet);

        //PAY 객체
        PaymentEntity payment = new PaymentEntity();
        payment.setBook(book);
        payment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        payment.setUpdatedAt(payment.getCreatedAt());

        paymentRepository.save(payment);

        //콘서트 예약 상태 변경
        book.setStatusCd(BookStatus.PAID);
        bookRepository.save(book);

        waitToken.endProcess();
        waitTokenRepository.save(waitToken);
//        waitQueueService.queueRefresh(ApiNo.PAYMENT);
        return true;
    }

}
