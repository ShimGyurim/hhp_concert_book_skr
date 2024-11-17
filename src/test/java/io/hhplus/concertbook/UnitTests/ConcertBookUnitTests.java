package io.hhplus.concertbook.UnitTests;

import io.hhplus.concertbook.application.facade.BookFacade;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.dto.ConcertScheduleDto;
import io.hhplus.concertbook.domain.dto.SeatDto;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.*;
import io.hhplus.concertbook.domain.service.BookService;
import io.hhplus.concertbook.domain.service.ConcertService;
import io.hhplus.concertbook.domain.service.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ConcertBookUnitTests {
    @Mock
    private ConcertItemRepository concertItemRepository;

    @InjectMocks
    private ConcertService concertService;


    @Mock
    private SeatRepository seatRepository;


    @Mock
    BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("날짜조회 성공")
    public void testGetAvailSchedule_Success() {
        ConcertItemEntity concertItem1 = new ConcertItemEntity();
        concertItem1.setConcertItemId(1L);
        //concertItem1.setAvailSeats(100);
        concertItem1.setConcert(new ConcertEntity());

        ConcertItemEntity concertItem2 = new ConcertItemEntity();
        concertItem2.setConcertItemId(2L);
        //concertItem2.setAvailSeats(50);
        concertItem2.setConcert(new ConcertEntity());

        Mockito.when(concertItemRepository.findByConcertD("2024-10-15")).thenReturn(Arrays.asList(concertItem1, concertItem2));

        List<ConcertScheduleDto> result = concertService.getAvailSchedule("2024-10-15");

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getConcertItemId());
        Assertions.assertEquals(100, result.get(0).getAvaliSeats());
        Assertions.assertEquals(2L, result.get(1).getConcertItemId());
        Assertions.assertEquals(50, result.get(1).getAvaliSeats());
    }

    @Test
    @DisplayName("날짜조회 실패")
    public void testGetAvailSchedule_NoConcerts() {
        Mockito.when(concertItemRepository.findByConcertD("2024-10-15")).thenReturn(Arrays.asList());

        List<ConcertScheduleDto> result = concertService.getAvailSchedule("2024-10-15");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("좌석조회 성공")
    public void testGetSeats_Success() {
        SeatEntity seat1 = new SeatEntity();
        seat1.setSeatId(1L);
        seat1.setSeatNo(10);
        seat1.setUse(true);

        SeatEntity seat2 = new SeatEntity();
        seat2.setSeatId(2L);
        seat2.setSeatNo(20);
        seat2.setUse(false);

        Mockito.when(seatRepository.findByConcertItem_ConcertItemId(1L)).thenReturn(Arrays.asList(seat1, seat2));

        List<SeatDto> result = concertService.getSeats(1L);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getSeatId());
        Assertions.assertEquals(10, result.get(0).getSeatNo());
        Assertions.assertTrue(result.get(0).isUse());
        Assertions.assertEquals(2L, result.get(1).getSeatId());
        Assertions.assertEquals(20, result.get(1).getSeatNo());
        Assertions.assertFalse(result.get(1).isUse());
    }

    @Test
    @DisplayName("좌석조회성공")
    public void testGetSeats_NoSeats() {
        Mockito.when(seatRepository.findByConcertItem_ConcertItemId(1L)).thenReturn(Arrays.asList());

        List<SeatDto> result = concertService.getSeats(1L);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("예약 정보 (락 객체) : 성공")
    public void testFindAndLockBook_Success() throws CustomException {
        Long bookId = 1L;
        BookEntity book = new BookEntity();
        book.setStatusCd(BookStatus.PREPAYMENT);

        Mockito.when(bookRepository.findByIdWithLock(bookId)).thenReturn(book);

        BookEntity result = concertService.findAndLockBook(bookId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(BookStatus.PREPAYMENT, result.getStatusCd());
        Mockito.verify(bookRepository).findByIdWithLock(bookId);
    }

    @Test
    @DisplayName("예약 정보 (락 객체) : 예약정보 못찾음")
    public void testFindAndLockBook_BookNotFound() {
        Long bookId = 1L;

        Mockito.when(bookRepository.findByIdWithLock(bookId)).thenReturn(null);

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            concertService.findAndLockBook(bookId);
        });

        Assertions.assertEquals(ErrorCode.BOOK_ERROR, exception.getErrorCode());
    }
}
