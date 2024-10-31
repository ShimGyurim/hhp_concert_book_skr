package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.dto.TokenDto;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
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

    @Transactional
    public TokenDto getToken(TokenDto tokenInDto) throws Exception {
        // 토큰을 받아 유효한게 있는지 확인
        // api 별로는 아예 신경 안스기
        if(tokenInDto == null) {
            throw new Exception("DTO정보없음");
        }

        WaitTokenEntity entity = waitTokenRepository.findByUser_UserNameAndServiceCd(tokenInDto.getUserName(),tokenInDto.getApiNo());

        if(entity == null || WaitStatus.EXPIRED.equals(entity.getStatusCd())) {
            WaitTokenEntity newEntity = new WaitTokenEntity();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String formDate = sdf.format(timestamp);

            UserEntity user = userRepository.findByUserName(tokenInDto.getUserName());

            if(user==null) {
                throw new CustomException(ErrorCode.USER_ERROR);
            }
            newEntity.setUser(user);
            newEntity.setServiceCd(tokenInDto.getApiNo());
            newEntity.setStatusCd(WaitStatus.WAIT);
            newEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            newEntity.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            newEntity.setToken(tokenInDto.getUserName()+formDate);

            waitTokenRepository.save(newEntity);

            TokenDto dto = new TokenDto();
            dto.setToken(newEntity.getToken());
            dto.setUserName(user.getUserName());
            dto.setApiNo(tokenInDto.getApiNo());

            //대기번호 리턴
            Long count = waitTokenRepository.countPreviousToken(tokenInDto.getApiNo(),newEntity.getUpdatedAt());


            if(count == 0) {
                Long countProcss = waitTokenRepository.countStatusToken(tokenInDto.getApiNo(),WaitStatus.PROCESS);
                if(countProcss==0) {
                    newEntity.setStatusCd(WaitStatus.PROCESS);
                    waitTokenRepository.save(newEntity);
                }
            }
            dto.setWaitNo(count.intValue());
            dto.setWaitStatus(newEntity.getStatusCd());
            return dto;

        }else{ //유효한 토큰이 존재하는 경우
            TokenDto dto = new TokenDto();
            dto.setToken(entity.getToken());
            dto.setUserName(entity.getUser().getUserName());
            dto.setApiNo(tokenInDto.getApiNo());

            //대기번호 리턴
            Long count = waitTokenRepository.countPreviousToken(tokenInDto.getApiNo(),entity.getUpdatedAt());

            if(count==0) {

                Long countProcss = waitTokenRepository.countStatusToken(tokenInDto.getApiNo(),WaitStatus.PROCESS);
                if(countProcss==0) {
                    entity.setStatusCd(WaitStatus.PROCESS);
                    waitTokenRepository.save(entity);
                }

            }
            dto.setWaitStatus(entity.getStatusCd());
            //TODO : 엔티티 dto 매퍼 구현
            return dto;
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

        if (WaitStatus.EXPIRED.equals(waitToken.getStatusCd())) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        } else if (WaitStatus.WAIT.equals(waitToken.getStatusCd())) {
            throw new CustomException(ErrorCode.TOKEN_WAIT);
        }

        if (!apiNo.equals(waitToken.getServiceCd())) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }

        return waitToken;
    }

    public void endProcess(WaitTokenEntity waitToken) {
        waitToken.endProcess();
        waitTokenRepository.save(waitToken);
    }

    public UserEntity findUserByToken(String token) throws CustomException {
        UserEntity user = waitTokenRepository.findUserinfoByToken(token);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_ERROR);
        }
        return user;
    }
}
