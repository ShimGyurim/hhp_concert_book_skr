package io.hhplus.concertbook.UnitTests;

import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.domain.entity.BookEntity;
import io.hhplus.concertbook.domain.entity.SeatEntity;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.repository.BookRepository;
import io.hhplus.concertbook.domain.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Timestamp;

public class BookUnitTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("예약 생성 : 성공")
    public void testCreateBooking_Success() {
        UserEntity user = new UserEntity();
        SeatEntity seat = new SeatEntity();
        BookEntity book = new BookEntity();
        book.setStatusCd(BookStatus.PREPAYMENT);
        book.setSeat(seat);
        book.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        book.setUpdatedAt(book.getCreatedAt());
        book.setUser(user);

        Mockito.when(bookRepository.save(ArgumentMatchers.any(BookEntity.class))).thenReturn(book);

        BookEntity result = bookService.createBooking(user, seat);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(BookStatus.PREPAYMENT, result.getStatusCd());
        Assertions.assertEquals(seat, result.getSeat());
        Assertions.assertEquals(user, result.getUser());
        Assertions.assertNotNull(result.getCreatedAt());
        Assertions.assertNotNull(result.getUpdatedAt());
        Mockito.verify(bookRepository).save(ArgumentMatchers.any(BookEntity.class));
    }
}
