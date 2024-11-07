package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.dto.TokenDto;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import io.hhplus.concertbook.domain.repository.RedisRepository;
import io.hhplus.concertbook.domain.repository.UserRepository;
import io.hhplus.concertbook.domain.repository.WaitTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Service
public class TokenService {

    @Autowired
    WaitTokenRepository waitTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WaitQueueService waitQueueService;

    @Autowired
    RedisRepository redisRepository;

    @Transactional
    public TokenDto getToken(TokenDto tokenInDto) throws Exception {
        // 토큰을 받아 유효한게 있는지 확인
        // api 별로는 아예 신경 안스기
        if(tokenInDto == null) {
            throw new Exception("DTO정보없음");
        }
        if(tokenInDto.getApiNo() == null) {
            throw new Exception("ApiNo정보없음");
        }

        boolean isWaitTokenExist = false;
        boolean isActiveTokenExist = false;

        WaitTokenEntity waitTokenEntity = waitTokenRepository.findByUser_UserLoginIdAndServiceCd(tokenInDto.getUserLoginId(),tokenInDto.getApiNo());

        if(waitTokenEntity != null && waitTokenEntity.getToken() != null) {
            isWaitTokenExist = redisRepository.isValueInWaitQueue(tokenInDto.getApiNo().toString(),waitTokenEntity.getToken());
            isActiveTokenExist = redisRepository.isValueInActiveQueue(tokenInDto.getApiNo().toString(),waitTokenEntity.getToken());
        }


        if(!isWaitTokenExist && !isActiveTokenExist) {

            WaitTokenEntity newEntity = new WaitTokenEntity();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String formDate = sdf.format(timestamp);

            UserEntity user = userRepository.findByUserLoginId(tokenInDto.getUserLoginId());

            if(user==null) {
                throw new CustomException(ErrorCode.USER_ERROR);
            }
            newEntity.setUser(user);
            newEntity.setServiceCd(tokenInDto.getApiNo());
            newEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            newEntity.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            newEntity.setToken(tokenInDto.getUserLoginId()+formDate);

            redisRepository.waitEnqueue(tokenInDto.getApiNo().toString(), newEntity.getToken());
            waitTokenRepository.save(newEntity); // token 저장

            TokenDto dto = new TokenDto();
            dto.setToken(newEntity.getToken());
            dto.setUserLoginId(user.getUserLoginId());
            dto.setApiNo(tokenInDto.getApiNo());

            dto.setWaitNo(redisRepository.getWaitQueueRank(tokenInDto.getApiNo().toString(),newEntity.getToken()));
            dto.setWaitStatus(WaitStatus.WAIT);
            return dto;

        }else if(isWaitTokenExist){ // 대기열에 유효한 토큰이 존재하는 경우
            TokenDto dto = new TokenDto();

            WaitTokenEntity entity = waitTokenRepository.findByToken(waitTokenEntity.getToken());

            dto.setToken(waitTokenEntity.getToken());
            dto.setUserLoginId(entity.getUser().getUserLoginId());
            dto.setApiNo(tokenInDto.getApiNo());

            //대기번호 리턴
            dto.setWaitNo(redisRepository.getWaitQueueRank(tokenInDto.getApiNo().toString(),waitTokenEntity.getToken()));

            dto.setWaitStatus(WaitStatus.WAIT);
            return dto;
        }else if(isActiveTokenExist){
            TokenDto dto = new TokenDto();

            WaitTokenEntity entity = waitTokenRepository.findByToken(waitTokenEntity.getToken());

            dto.setToken(waitTokenEntity.getToken());
            dto.setUserLoginId(entity.getUser().getUserLoginId());
            dto.setApiNo(tokenInDto.getApiNo());

            //대기번호 리턴
            dto.setWaitNo(0);

            dto.setWaitStatus(WaitStatus.PROCESS);
            return dto;
        }else {
            throw new Exception("상태이상");
        }

    }

    public WaitTokenEntity validateToken(String token, ApiNo apiNo) throws CustomException {
        if (token == null) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }

        WaitTokenEntity waitToken = waitTokenRepository.findByToken(token);
        if (waitToken == null) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }

        boolean isActiveTokenExist = redisRepository.isValueInActiveQueue(apiNo.toString(),token);
        if(!isActiveTokenExist) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }

        if (!apiNo.equals(waitToken.getServiceCd())) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }

        return waitToken;
    }

    public void endProcess(WaitTokenEntity waitToken) {
        redisRepository.waitRemove(waitToken.getServiceCd().toString(),waitToken.getToken());
        redisRepository.activeRemove(waitToken.getServiceCd().toString(),waitToken.getToken());
        waitTokenRepository.deleteById(waitToken.getTokenId()); //삭제
    }

    public UserEntity findUserByToken(String token) throws CustomException {
        UserEntity user = waitTokenRepository.findUserinfoByToken(token);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_ERROR);
        }
        return user;
    }
}
