package io.hhplus.concertbook.IntegrationTests;

import io.hhplus.concertbook.ConcertBookApp;
import io.hhplus.concertbook.domain.repository.ConcertRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@SpringBootTest(classes = ConcertBookApp.class)
public class TransactionTest  {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTest.class);

    @Autowired
    private ConcertRepository concertRepository;

    @Test
    public void run() throws Exception {
        int maxTransactions = 10000;
        int step = 1000;

        try {
            while (true) {
                logger.info("Testing with {} transactions", maxTransactions);
                if (!testTransactions(maxTransactions)) {
                    logger.info("Max transactions limit reached: {}", maxTransactions);
                    break;
                }
                maxTransactions += step;
            }
        }catch (Exception e) {
            e.getStackTrace();
        }

    }

    private boolean testTransactions(int transactionCount) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        try {
            for (int i = 0; i < transactionCount; i++) {
                executorService.submit(this::executeTransaction);
            }

            executorService.shutdown();
            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                logger.warn("Executor service did not terminate");
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("Error occurred during transaction test", e);
            return false;
        }
    }

    @Transactional
    public void executeTransaction() {
        concertRepository.findAll();
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}