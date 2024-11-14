## 자주 조회되는 쿼리 목록

1. 대상 쿼리 : 콘서트 일정 조회 
2. 지정 사유: 현재 앱에서 가장 조회할 자료도 많고 자주 조회되는 쿼리 
3. 입력 정보 : 날짜 (character 8자리) 
4. 출력 정보 : 대상 concert_item_id 목록 
5. 대상 테이블 : 콘서트 스케줄 정보 concert_item

![image](https://github.com/user-attachments/assets/f17a791c-d1f2-4d7c-abcd-c03b9cceb8a5)

1. 카디널리티 : 약 6백만 건 concert 스케줄 정보 

![image](https://github.com/user-attachments/assets/54b632bd-79f8-4a74-9058-7fe5a11a06fc)
1. 자료 분포

![image](https://github.com/user-attachments/assets/2f61f8fe-a82c-4772-9613-52e81415897d)

<인덱스 없을 때 >

---

1. 기본 인덱스 : PK 인 concert_item_id 와 FK 인 concert_id 의 두 개의 인덱스 존재  

![image](https://github.com/user-attachments/assets/c2fe86f5-ec5d-4093-8db5-44bd57308685)

쿼리 시간 테스트 : 동일 로직을 http 요청으로 측정시간 결과 나오게 /dataTest 에 임시로 구현해 놓음 

```jsx
    @Test
    @DisplayName("인덱스 적용 전후 속도체크 테스트")
    public void testGetToken_NewToken() throws Exception {
        long startTime = System.currentTimeMillis();
        List<ConcertItemEntity> results = concertItemRepository.findByConcertD("20241003");
        long endTime = System.currentTimeMillis();
        log.info("Execution time: " + (endTime - startTime) + "ms");
    }
```

1. 경과시간 : (입력값 20241003) 9493 ms  

![image](https://github.com/user-attachments/assets/209e467b-6fed-4f98-90ad-d29a187fcea9)

1. explain 결과: full scan 

![image](https://github.com/user-attachments/assets/177399f0-78db-4085-8793-81e63334a225)

### <인덱스1 : concertD 컬럼>

---

1. 설정 근거: 카디널리티는 낮지만 가장 많은 row가 필터링 되는 컬럼 

![image](https://github.com/user-attachments/assets/04f9f76c-7e53-4a93-98f0-61eb4912b7b0)

1. 경과시간: 8559 ms 

![image](https://github.com/user-attachments/assets/b9c21cdf-53c4-4062-9de6-df9c26116fc5)

1. explain 결과 : IDX_DATE (현재 설정 인덱스) 탔음

![image](https://github.com/user-attachments/assets/4ee92987-72eb-489f-b650-f032f4c4498a)

1. 결론: 소폭 줄어들긴 했으나 여전히 불만족스러운 속도 

다른 날짜로 시도 : ( 20241001 )

---

1. 인덱스 적용 전 : 9847 ms 소요 

![image](https://github.com/user-attachments/assets/c638604f-8a3e-45ab-a930-96740f9e7143)

1. 인덱스 적용 후 :  8317 ms 소요 

![image](https://github.com/user-attachments/assets/8d489d4e-eacf-4a76-abbf-edc7458298cb)

1. 결론: 20231003 날짜 입력 시와 비슷한 결과 

### <인덱스2: concertD, concert_item_id 복합인덱스>

---

![image](https://github.com/user-attachments/assets/9f1a4d66-a51f-423f-80c8-fd01655f7dae)

1. explain 결과: index 탐 

![image](https://github.com/user-attachments/assets/a6d6b9cc-a0fe-4f4d-8687-95e83c1efa7c)
1. 인덱스 반영 전 : 9928 ms 소요

![image](https://github.com/user-attachments/assets/5e70ad24-d3ac-425d-a66e-ad97e2d4ad5a)

1. 인덱스 반영 후 : 8250 ms 소요

![image](https://github.com/user-attachments/assets/2f36a6c5-a8a5-46d9-a799-0ef99f7ee963)

1. 결론: concertD 단독으로 붙일 때와 차이 없음 

### 최종결론

---

concertD 컬럼 단독으로 index 를 거는게 최선이라고 생각함.
