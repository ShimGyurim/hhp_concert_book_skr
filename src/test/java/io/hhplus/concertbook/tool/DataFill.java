package io.hhplus.concertbook.tool;

import io.hhplus.concertbook.ConcertBookApp;
import io.hhplus.concertbook.domain.entity.ConcertEntity;
import io.hhplus.concertbook.domain.entity.ConcertItemEntity;
import io.hhplus.concertbook.domain.entity.SeatEntity;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.repository.ConcertItemRepository;
import io.hhplus.concertbook.domain.repository.ConcertRepository;
import io.hhplus.concertbook.domain.repository.SeatRepository;
import io.hhplus.concertbook.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest(classes = ConcertBookApp.class)
public class DataFill {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userFill() { //사용자 채우기

        for (int i=0; i<1000;i++) {
            UserEntity tempUser = userRepository.findByUserLoginId(Integer.toString(i));
            if(tempUser != null) continue;
            UserEntity user = new UserEntity();
            user.setUserLoginId(Integer.toString(i));
            userRepository.save(user);
        }
    }

    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private ConcertItemRepository concertItemRepository;

    @Test
    public void concertDateFill() { // 콘서트 일정 채우기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        ConcertEntity concert = concertRepository.findById(1L).get();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String dateString = date.format(formatter);
            ConcertItemEntity concertItem = new ConcertItemEntity();
            concertItem.setConcert(concert);
            concertItem.setConcertD(dateString);
            concertItem.setConcertT("180000");
            concertItemRepository.save(concertItem);
        }
    }

    @Test
    public void concertTimeFill() { // 콘서트 일정 (시간별) 채우기
        ConcertEntity concert = concertRepository.findById(1L).get();

        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                for (int second = 0; second < 60; second++) {
                    String timeString = String.format("%02d%02d%02d", hour, minute, second);
                    ConcertItemEntity concertItem = new ConcertItemEntity();
                    concertItem.setConcertD("20241130");
                    concertItem.setConcertT(timeString);
                    concertItem.setConcert(concert);
                    concertItemRepository.save(concertItem);
                }
            }
        }
    }

    @Autowired
    private SeatRepository seatRepository;

    @Test
    public void concertSeatFill() { // 콘서트 좌석 채우기
        ConcertEntity concert = concertRepository.findById(1L).get();
//        int hour = 11;
        for (int hour = 0; hour < 24; hour++) {
            if(hour==11) continue;
            for (int minute = 0; minute < 60; minute++) {
                for (int second = 0; second < 60; second++) {
                    String timeString = String.format("%02d%02d%02d", hour, minute, second);
                    List<ConcertItemEntity> concertItemEntityList = concertItemRepository.findByConcertDAndConcertT("20241130",timeString);
                    ConcertItemEntity concertItem = concertItemEntityList.get(0);

                    for (int seatNo = 1; seatNo <= 2; seatNo++) {
                        SeatEntity seat = new SeatEntity();
                        seat.setConcertItem(concertItem);
                        seat.setSeatNo(seatNo);
                        seatRepository.save(seat);
                    }
                }
            }
        }
    }
}
