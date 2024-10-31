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

    public PaymentEntity createPayment(BookEntity book) {
        PaymentEntity payment = new PaymentEntity();
        payment.setBook(book);
        payment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        payment.setUpdatedAt(payment.getCreatedAt());
        paymentRepository.save(payment);
        return payment;
    }
}
