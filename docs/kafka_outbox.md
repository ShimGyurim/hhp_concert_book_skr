### 커밋 안내
4173e751 kafka 초기세팅 부분

44c4a788 outbox 생성 

6dee7ee8 예약 스케줄러 구현 부분

86e7debd 결제 기능 kafka 적용 부분

### 스프링이벤트 + 카프카 메시지 발행 

- facade 로직에서 outbox 엔티티 저장 및 이벤트 호출

```jsx
        BookEvent bookEvent = outboxService.bookOutboxService(book,seat,waitToken);
        eventPublisher.publishEvent(bookEvent);
```

- 이벤트 처리 부분 에서 kafka 발행

```jsx
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void bookSuccessApiHandler(BookEvent bookEvent) throws InterruptedException {
        bookProducer.send("BOOK_SAVE",bookEvent.getMessageQueueKey(),bookEvent.getOutboxId());
    }
```

- kafka Consumer 에서 예약내역 저장 로직 호출

```jsx
    @KafkaListener(topics = "BOOK_SAVE", groupId = "group_1")
    public void listen(long outboxId) throws JsonProcessingException, InterruptedException {
        OutboxEntity outboxEntity = outboxRepository.findById(outboxId).get();
        outboxEntity.setStatus("PUBLISHED"); 
        
        outboxRepository.save(outboxEntity);
        ObjectMapper objectMapper = new ObjectMapper();
        BookEvent bookEvent = objectMapper.readValue(outboxEntity.getPayLoad(), BookEvent.class);
        // 예약내역 저장 로직 호출
        sendBookInfo("id "+bookEvent.getBook().getBookId()+" 건 예약성공:"+bookEvent.getBook().toString()); 
        
        outboxEntity.setStatus("COMPLETE");
        outboxRepository.save(outboxEntity);
    }

```

### transactional outbox pattern 적용
```
    public BookEvent bookOutboxService (BookEntity book, SeatEntity seat, WaitTokenEntity waitToken) throws JsonProcessingException {
        OutboxEntity outboxEntity = new OutboxEntity();
        outboxEntity.setMqKey("BOOK:"+seat.getSeatId());
        outboxEntity.setTopic("BOOK_SAVE");
        outboxEntity.setType("BOOK");
        outboxEntity.setStatus("INIT");
        outboxEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        ObjectMapper objectMapper = new ObjectMapper();
        BookEvent bookEvent = new BookEvent(book,waitToken,-1L,outboxEntity.getMqKey());
        OutboxEntity newOutbox = outboxRepository.save(outboxEntity);
        bookEvent.setOutboxId(newOutbox.getOutboxId());
        
        // payload 부분은 json 활용하여 event 객체 저장 처리
        String payLoad = objectMapper.writeValueAsString(bookEvent);
        newOutbox.setPayLoad(payLoad);
        outboxRepository.save(newOutbox);

        return bookEvent;
    }
```

### 발행 실패 케이스 재처리 (scheduler 사용)
```
    @Scheduled(fixedRate = 20000) // 20초마다 5분이 지나도 INIT인 상태를 찾아서 kafka 발행
    public void BookPubSchedule() throws JsonProcessingException {
        List<OutboxEntity> outboxEntityList = outboxRepository.findAllByTopicAndStatus("BOOK_SAVE","INIT");

        for (OutboxEntity outbox : outboxEntityList) {
            if(outbox.getCreatedAt() == null) continue;
            LocalDateTime someMinAgo = LocalDateTime.now().minus(10, ChronoUnit.SECONDS);
            if(outbox.getCreatedAt().toLocalDateTime().isBefore(someMinAgo)) {
                bookProducer.send("BOOK_SAVE",outbox.getMqKey(),outbox.getOutboxId());
            }
        }
    }
```

**위 예약기능에 적용한 로직을 결제 로직에도 동일하게 적용**