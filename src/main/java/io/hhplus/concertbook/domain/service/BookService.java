package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.domain.entity.BookEntity;
import io.hhplus.concertbook.domain.entity.SeatEntity;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    public void updateBookStatus(BookEntity book, BookStatus status) {
        book.setStatusCd(status);
        bookRepository.save(book);
    }

    public BookEntity createBooking(UserEntity user, SeatEntity seat) {
        BookEntity book = new BookEntity();
        book.setStatusCd(BookStatus.PREPAYMENT);
        book.setSeat(seat);
        book.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        book.setUpdatedAt(book.getCreatedAt());
        book.setUser(user);
        bookRepository.save(book);
        return book;
    }
}
