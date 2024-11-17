package io.hhplus.concertbook.PerformTests;

import io.hhplus.concertbook.ConcertBookApp;
import io.hhplus.concertbook.IntegrationTests.TransactionTest;
import io.hhplus.concertbook.domain.entity.ConcertEntity;
import io.hhplus.concertbook.domain.entity.ConcertItemEntity;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.repository.ConcertItemRepository;
import io.hhplus.concertbook.domain.repository.ConcertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(classes = ConcertBookApp.class)
public class ConcertLookupPerformTests {


    private static final Logger log = LoggerFactory.getLogger(TransactionTest.class);

    @Autowired
    ConcertRepository concertRepository;
    @Autowired
    ConcertItemRepository concertItemRepository;

    @Test
    @DisplayName("다수 콘서트 건 삽입")
    public void setUp() {
        ConcertEntity concert1 = new ConcertEntity();
        ConcertEntity concert2 = new ConcertEntity();
        ConcertEntity concert3 = new ConcertEntity();
        concertRepository.save(concert1);
        concertRepository.save(concert2);
        concertRepository.save(concert3);

        String concertDate = "20241030";

        for (int i =0; i<10000000; i++) {
            ConcertItemEntity concertItem = new ConcertItemEntity();
            if(i%3==1){
                concertDate = "20241004";
                concertItem.setConcert(concert1);
            }else if(i%3==2){
                concertDate = "20241005";
                concertItem.setConcert(concert2);
            }else {
                concertDate = "20241006";
                concertItem.setConcert(concert3);
            }

            concertItem.setConcertD(concertDate);
            concertItem.setConcertT("000000");
//            concertItem.setAllseats(0);
//            concertItem.setAvailSeats(50);
            concertItemRepository.save(concertItem);
        }
        for (int i =0; i<1000000; i++) {
            ConcertItemEntity concertItem = new ConcertItemEntity();
            concertItem.setConcert(concert2);
            concertItem.setConcertD(concertDate);
            concertItem.setConcertT("000000");
//            concertItem.setAllseats(0);
//            concertItem.setAvailSeats(50);
            concertItemRepository.save(concertItem);
        }
        for (int i =0; i<1000000; i++) {
            ConcertItemEntity concertItem = new ConcertItemEntity();
            concertItem.setConcert(concert3);
            concertItem.setConcertD(concertDate);
            concertItem.setConcertT("000000");
//            concertItem.setAllseats(0);
//            concertItem.setAvailSeats(50);
            concertItemRepository.save(concertItem);
        }
    }

    @Test
    @DisplayName("인덱스 적용 전후 속도체크 테스트")
    public void testGetToken_NewToken() throws Exception {
        long startTime = System.currentTimeMillis();
        List<ConcertItemEntity> results = concertItemRepository.findByConcertD("20241003");
        long endTime = System.currentTimeMillis();
        log.info("Execution time: " + (endTime - startTime) + "ms");
    }
}
