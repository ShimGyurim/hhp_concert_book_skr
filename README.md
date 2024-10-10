
## 마일스톤

10/8 요구사항분석, 마일스톤 작성 

10/9 시퀀스 다이어그램 작성, ERD 작성 

10/10 ERD 보완 , mock api 작성 및 단위테스트 작성  

10/11 폴링을 위한 스케줄러 구현방법 구상 

10/12 

10/13 토큰 부여 api 구현 ,예약가능날짜,좌석 api 구현 

10/14 잔액 충전 / 조회 API  , 결제 api 구현 

10/15 좌석 예약 요청 api 

10/16 단위테스트 작성

10/17 동시정제어 테스트 작성 

10/18~10/24 추가 코드 작성 (5주차) : filter , interceptor 등

## 개별 플로우 차트

- 토큰발급 api
![image](https://github.com/user-attachments/assets/e61e64b7-755e-4c33-a907-fa3a634a4cfa)


- 콘서트 날짜별 / 좌석 조회 api 
![image](https://github.com/user-attachments/assets/401db229-bf28-474c-b536-2baafa1116f3)


- 콘서트 예약 & 결제 api
![image](https://github.com/user-attachments/assets/5ccbf5c2-a272-47ff-91cd-b763c93dccf7)


- 잔액 충전 / 조회 api
![image](https://github.com/user-attachments/assets/e86e9060-60d6-487b-ac1d-b9a0cdeaca45)


## 전체 플로우 차트
![image](https://github.com/user-attachments/assets/9c1ed351-55bf-482d-95b4-87ecc8a5322b)


## ERD
![image](https://github.com/user-attachments/assets/cfdafcee-6e1b-4758-98ad-3b188896a48f)


## 기술스택
- java 
- spring boot
- jpa 
- h2 db


## 패키지 구조
presentation
  controller
application 
  facade (필요시)
domain
  service 
  entity (pojo)
infrasructure 
  entity (jpa)
  mapper
  

## API 명세
![image](https://github.com/user-attachments/assets/e54b9229-707c-474a-b68f-5ded399fec3b)
![image](https://github.com/user-attachments/assets/fe595276-86ca-4992-a033-9c2c6a2024cc)
