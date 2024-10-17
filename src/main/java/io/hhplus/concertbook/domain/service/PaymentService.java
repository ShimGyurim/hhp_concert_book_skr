package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.NoTokenException;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.BookRepo;
import io.hhplus.concertbook.domain.repository.WaitTokenRepo;
import io.hhplus.concertbook.domain.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class PaymentService {

    @Autowired
    BookRepo bookRepo;

    @Autowired
    WaitTokenRepo waitTokenRepo;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    WaitQueueService waitQueueService;

    @Transactional
    public boolean pay(String token, Long bookId) throws Exception {
        if(token == null){
            throw new NoTokenException();
        }
        waitQueueService.queueRefresh(ApiNo.PAYMENT);

        Optional<BookEntity> bookEntityOptional = bookRepo.findById(bookId);
        BookEntity book = bookEntityOptional.get();
        UserEntity userBook = book.getUser();

        if(book == null) {
            throw new Exception();
        }
        if(!BookStatus.PREPAYMENT.equals(book.getStatusCd())){
            throw new Exception("결제할 항목없음");
        }
        WaitTokenEntity waitToken = waitTokenRepo.findByToken(token);
        if(waitToken == null) {
            throw new NoTokenException(); //TODO: http 500 리턴 코드가 있는데 괜찮을지?
        }

        if(WaitStatus.EXPIRED.equals(waitToken.getStatusCd())) {
            throw new Exception("토큰만료");
        }else if(WaitStatus.WAIT.equals(waitToken.getStatusCd())) {
            throw new Exception("토큰대기중");
        }

        UserEntity userToken = waitToken.getUser();

        if(userBook == null || userToken == null) {
            throw new Exception("사용자정보없음");
        }else if(userBook.getUserId() != userToken.getUserId()) {
            throw new Exception("권한없음");
        }
        //결제로직

        //돈있는지 확인
        SeatEntity seat = book.getSeat();
        if(seat == null) {
            throw new Exception("좌석정보없음");
        }
        ConcertEntity concert = seat.getConcertItem().getConcert();
        if(concert == null) {
            throw new Exception("콘서트없음");
        }
        long fee = concert.getFee();

        WalletEntity wallet = walletRepo.findByUser_UserId(userBook.getUserId());
        if(wallet == null) {
            throw new Exception("잔액정보없음");
        }
        if(wallet.getAmount() < fee) {
            throw new Exception("잔액부족");
        }

        //지갑 금액 차감
        wallet.setAmount(wallet.getAmount()-fee);
        walletRepo.save(wallet);

        //콘서트 예약 상태 변경
        book.setStatusCd(BookStatus.PAID);

        waitToken.endProcess();
//        waitQueueService.queueRefresh(ApiNo.PAYMENT);
        return true;
    }

}