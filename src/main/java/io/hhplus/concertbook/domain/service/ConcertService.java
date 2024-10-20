package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.BookStatus;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.NoTokenException;
import io.hhplus.concertbook.domain.dto.ConcertScheduleDto;
import io.hhplus.concertbook.domain.dto.SeatDto;
import io.hhplus.concertbook.domain.entity.*;
import io.hhplus.concertbook.domain.repository.BookRepo;
import io.hhplus.concertbook.domain.repository.ConcertItemRepo;
import io.hhplus.concertbook.domain.repository.SeatRepo;
import io.hhplus.concertbook.domain.repository.WaitTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConcertService {

    @Autowired
    ConcertItemRepo concertItemRepo;

    @Autowired
    SeatRepo seatRepo;

    @Autowired
    WaitTokenRepo waitTokenRepo;

    @Autowired
    WaitQueueService waitQueueService;

    @Autowired
    BookRepo bookRepo;

    public List<ConcertScheduleDto> getAvailSchedule(String scheduleDate){
        List<ConcertItemEntity> concertItems = concertItemRepo.findByConcertD(scheduleDate);

       List<ConcertScheduleDto> concertSchdules = concertItems.stream()
                .map(item -> {
                    Long id = item.getConcertItemId();
                    int availSeats = item.getAvailSeats();
                    //TODO : getConcert 없으면 exception

                    ConcertScheduleDto concertScheduleDto = new ConcertScheduleDto();

                    concertScheduleDto.setConcertItemId(id);
                    concertScheduleDto.setAvaliSeats(availSeats);
                    return concertScheduleDto;
                })
                .collect(Collectors.toList());


        return concertSchdules; //TODO: 스케줄id 말고 이름도 출력
    }

    public List<SeatDto> getSeats(long scheduleId){ //출력: 좌석id랑, 좌석 no 리스트
        List<SeatEntity> seatItems = seatRepo.findByConcertItem_ConcertItemId(scheduleId);

        List<SeatDto> seatNos = seatItems.stream()
                .map(item -> {
                    SeatDto dto = new SeatDto();
                    dto.setSeatId(item.getSeatId());
                    dto.setSeatNo(item.getSeatNo());
                    dto.setUse(item.isUse());
                    return dto;
                })
                .collect(Collectors.toList());

        return seatNos;
    }

    @Transactional
    public long book(String token,long seatId) throws Exception {
        //TODO: token 이랑 userid 를 같이 받는게 옳은건지?

        if(token == null){
            throw new NoTokenException();
        }

//        waitQueueService.queueRefresh(ApiNo.BOOK); // 큐 새로고침

        WaitTokenEntity waitToken = waitTokenRepo.findByToken(token);

        if(waitToken == null) {
            throw new NoTokenException();
        }

        if(WaitStatus.EXPIRED.equals(waitToken.getStatusCd())) {
            throw new Exception("토큰만료");
        }else if(WaitStatus.WAIT.equals(waitToken.getStatusCd())) {
            throw new Exception("토큰대기중");
        }
        if(!ApiNo.BOOK.equals(waitToken.getServiceCd())){
            throw new Exception("다른 서비스 토큰");
        }

        UserEntity user = waitTokenRepo.findUserinfoByToken(token);
        SeatEntity seat = seatRepo.findById(seatId).get();

        if(user==null){
            throw new Exception();
        }
        if(seat==null){
            throw new Exception();
        }
        if(seat.isUse()){
            throw new Exception("좌석점유중");
        }

        seat.setUse(true);
        seatRepo.save(seat);

        BookEntity book = new BookEntity();
        book.setStatusCd(BookStatus.PREPAYMENT);
        book.setSeat(seat);
        book.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        book.setUpdatedAt(book.getCreatedAt());
        book.setUser(user);

        bookRepo.save(book);

        waitToken.endProcess(); // 프로세스 end 처리 (다음
        waitTokenRepo.save(waitToken);
        return book.getBookId();
    }

    
    //TEST용
    public void seatInsert(Long concertItemId) {
        for (int i =1 ; i<=50; i++) {
            SeatEntity seat = new SeatEntity();
            seat.setSeatNo(i);
            seat.setConcertItem(concertItemRepo.findById(concertItemId).get());
            seat.setUse(false);
            seatRepo.save(seat);
        }
    }
}
