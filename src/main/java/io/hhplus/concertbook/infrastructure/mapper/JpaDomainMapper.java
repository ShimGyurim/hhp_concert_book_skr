package io.hhplus.concertbook.infrastructure.mapper;

import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.infrastructure.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JpaDomainMapper {
    @Autowired
    private ModelMapper modelMapper;

    public BookEntity bookEntityToPojo (Book book) {
        BookEntity bookEntity = modelMapper.map(book,BookEntity.class);
        bookEntity.setBookId(book.getBookId());
        return bookEntity;
    }
    public Book bookPojoToEntity (BookEntity bookEntity) {
        Book book = modelMapper.map(bookEntity,Book.class);
        book.setBookId(bookEntity.getBookId());
        return book;
    }


    public ConcertEntity concertEntityToPojo (Concert concert) {
        ConcertEntity concertEntity = modelMapper.map(concert,ConcertEntity.class);
        concertEntity.setConcertId(concert.getConcertId());
        return concertEntity;
    }
    public Concert concertPojoToEntity (ConcertEntity concertEntity) {
        Concert concert = modelMapper.map(concertEntity,Concert.class);
        concert.setConcertId(concertEntity.getConcertId());
        return concert;
    }


    public ConcertItemEntity concertItemEntityToPojo (ConcertItem concertItem) {
        ConcertItemEntity concertItemEntity = modelMapper.map(concertItem,ConcertItemEntity.class);
        concertItemEntity.setConcertItemId(concertItem.getConcertItemId());
        return concertItemEntity;
    }
    public ConcertItem concertItemPojoToEntity (ConcertItemEntity concertItemEntity) {
        ConcertItem concertItem = modelMapper.map(concertItemEntity,ConcertItem.class);
        concertItem.setConcertItemId(concertItemEntity.getConcertItemId());
        return concertItem;
    }


    public PaymentEntity paymentEntityToPojo (Payment payment) {
        PaymentEntity paymentEntity = modelMapper.map(payment,PaymentEntity.class);
        paymentEntity.setPaymentId(payment.getPaymentId());
        return paymentEntity;
    }
    public Payment paymentPojoToEntity (PaymentEntity paymentEntity) {
        Payment payment = modelMapper.map(paymentEntity,Payment.class);
        payment.setPaymentId(paymentEntity.getPaymentId());
        return payment;
    }


    public SeatEntity seatEntityToPojo (Seat seat) {
        SeatEntity seatEntity = modelMapper.map(seat,SeatEntity.class);
        seatEntity.setSeatId(seat.getSeatId());
        return seatEntity;
    }
    public Seat seatPojoToEntity (SeatEntity seatEntity) {
        Seat seat = modelMapper.map(seatEntity,Seat.class);
        seat.setSeatId(seatEntity.getSeatId());
        return seat;
    }

    public UserEntity userEntityToPojo (User user) {
        UserEntity userEntity = modelMapper.map(user,UserEntity.class);
        userEntity.setUserId(user.getUserId());
        return userEntity;
    }
    public User userPojoToEntity (UserEntity userEntity) {
        User user = modelMapper.map(userEntity,User.class);
        user.setUserId(userEntity.getUserId());
        return user;
    }

    public WaitTokenEntity waitTokenEntityToPojo (WaitToken waitToken) {
        WaitTokenEntity waitTokenEntity = modelMapper.map(waitToken,WaitTokenEntity.class);
        waitTokenEntity.setTokenId(waitToken.getTokenId());
        return waitTokenEntity;
    }
    public WaitToken waitTokenPojoToEntity (WaitTokenEntity waitTokenEntity) {
        WaitToken waitToken = modelMapper.map(waitTokenEntity,WaitToken.class);
        waitToken.setTokenId(waitTokenEntity.getTokenId());
        return waitToken;
    }

    public WalletEntity walletEntityToPojo (Wallet wallet) {
        WalletEntity walletEntity = modelMapper.map(wallet,WalletEntity.class);
        walletEntity.setWalletId(wallet.getWalletId());
        return walletEntity;
    }
    public Wallet walletPojoToEntity (WalletEntity walletEntity) {
        Wallet wallet = modelMapper.map(walletEntity,Wallet.class);
        wallet.setWalletId(walletEntity.getWalletId());
        return wallet;
    }
}
