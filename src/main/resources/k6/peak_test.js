import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend } from 'k6/metrics';

// 콘서트 조회 -> 좌석조회 -> 로그인 -> 토큰부여 -> 예약 최고부하테스트
export const options = {
    vus: 1000, // 가상 사용자 수
    iterations: 1000, // 반복 횟수
};

let concertResTime = new Trend('concert_response_time');
let seatResTime = new Trend('seat_response_time');
let loginResTime = new Trend('login_response_time');
let tokenResTime = new Trend('token_response_time');
let bookResTime = new Trend('book_response_time');

export default function () {
    let concertRes = http.get('http://localhost:10180/concert/date?concertd=20241130');

    concertResTime.add(concertRes.timings.duration);

    let concertResBody = JSON.parse(concertRes.body);

    let concertItemId = concertResBody.data[Math.floor(__VU / 2)].concertItemId;
    let avaliSeats = concertResBody.data[Math.floor(__VU / 2)].avaliSeats;

    check(concertRes, {
            'status is 200': (r) => r.status === 200
    });

    let seatRes = http.get('http://localhost:10180/concert/seat?itemid='+concertItemId);

    seatResTime.add(seatRes.timings.duration);

    let seatResBody = JSON.parse(seatRes.body);

    let seatId = seatResBody.data[0].seatId;
    let seatNo = seatResBody.data[0].seatNo;
    let use = seatResBody.data[0].use;

    check(seatRes, {
                'status is 200': (r) => r.status === 200
        });

    // 1. 로그인 요청
    let loginRes = http.post('http://localhost:10180/login', JSON.stringify({
        username: `${__VU}`
    }), {
        headers: { 'Content-Type': 'application/json' }
    });

    loginResTime.add(loginRes.timings.duration);

    let isSuccessLogin = check(loginRes, {
        'login successful': (res) => res.status === 200
    });

    if(!isSuccessLogin) {
//        console.log('login fail: '+`${__VU}`);
    }

    // 2. 토큰 요청
    let tokenRes = http.post('http://localhost:10180/auth/token', JSON.stringify({
        userLoginId: `${__VU}`,
        apiServiceName: 'BOOK'
    }), {
        headers: { 'Content-Type': 'application/json' }
    });

    tokenResTime.add(tokenRes.timings.duration);

    let isSuccessToken = check(tokenRes, {
        'token request successful': (res) => res.status === 200
    });

    if(!isSuccessToken) {
//        console.log('token fail: '+`${__VU}`);
    }

    let tokenResBody = JSON.parse(tokenRes.body);


//    console.log(`PARAMETER: ${tokenRes.body}`);
    let token = tokenResBody.data.token;

    // 3. 콘서트 예약 요청
    let bookRes = http.post('http://localhost:10180/concert/book', JSON.stringify({
        seatId: seatId,
        token: token
    }), {
        headers: { 'Content-Type': 'application/json' }
    });

    bookResTime.add(bookRes.timings.duration);

    let isSuccessBook = check(bookRes, {
        'booking successful': (res) => res.status === 200
    });

    if(!isSuccessBook) {
//        console.log('book fail: '+`${__VU}` + " seatId: " + seatId+" token: "+token);
    }

//    console.log(`RESPONSE : ${JSON.stringify(bookRes)}`);

    sleep(1);
}
