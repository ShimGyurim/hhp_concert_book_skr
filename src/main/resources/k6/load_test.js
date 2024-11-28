import http from 'k6/http';
import { sleep } from 'k6';
import { check } from 'k6';
import { Trend } from 'k6/metrics';

// 충전 & 조회 부하테스트
export let options = {
    vus: 100, // 가상 사용자 수
    duration: '2m', // 테스트 지속 시간
    rps: 50, // 초당 요청 수
    thresholds: {
        'http_req_duration{scenario:charge}': ['p(95)<1000'], // 첫 번째 API의 95번째 백분위수 응답 시간 목표
        'http_req_duration{scenario:balance}': ['p(95)<1000'] // 두 번째 API의 95번째 백분위수 응답 시간 목표
    }
};

let chargeResTime = new Trend('charge_response_time');
let balanceResTime = new Trend('balance_response_time');

export default function () {
    // username 값을 1부터 100까지 순차적으로 설정
    let username = __VU % 100 + 1;

    // 첫 번째 API 호출
    let chargeRes = http.get(`http://localhost:10180/money/charge?username=${username}&chargeamt=100`, {
        tags: { scenario: 'charge' } // 첫 번째 API에 태그 추가
    });

    chargeResTime.add(chargeRes.timings.duration);

    check(chargeRes, {
        'charge request successful': (res) => res.status === 200
    });

//    console.log(`RESPONSE : ${JSON.stringify(chargeRes)}`);

    // 두 번째 API 호출
    let balanceRes = http.get(`http://localhost:10180/money/balance?username=${username}`, {
        tags: { scenario: 'balance' } // 두 번째 API에 태그 추가
    });

    balanceResTime.add(balanceRes.timings.duration);

    check(balanceRes, {
        'balance request successful': (res) => res.status === 200
    });

    sleep(1);
}