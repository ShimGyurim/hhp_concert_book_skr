package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.constant.GlobalConstant;
import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.NoUserException;
import io.hhplus.concertbook.domain.dto.TokenDto;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import io.hhplus.concertbook.domain.repository.UserRepo;
import io.hhplus.concertbook.domain.repository.WaitTokenRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;

@Service
public class TokenService {

    @Autowired
    WaitTokenRepo waitTokenRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    WaitQueueService waitQueueService;

    @Transactional
    public TokenDto getToken(TokenDto tokenInDto) throws Exception {
        // 토큰을 받아 유효한게 있는지 확인
        // api 별로는 아예 신경 안스기
        if(tokenInDto == null) {
            throw new Exception();
        }

//        waitQueueService.queueRefresh(tokenInDto.getApiNo());

        WaitTokenEntity entity = waitTokenRepo.findByUser_UserNameAndServiceCd(tokenInDto.getUserName(),tokenInDto.getApiNo());

        if(entity == null || WaitStatus.EXPIRED.equals(entity.getStatusCd())) {
            WaitTokenEntity newEntity = new WaitTokenEntity();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String formDate = sdf.format(timestamp);

            UserEntity user = userRepo.findByUserName(tokenInDto.getUserName());

            if(user==null) {
                throw new NoUserException("사용자없음");
            }
            newEntity.setUser(user);
            newEntity.setServiceCd(tokenInDto.getApiNo());
            newEntity.setStatusCd(WaitStatus.WAIT);
            newEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            newEntity.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            newEntity.setToken(tokenInDto.getUserName()+formDate);

            waitTokenRepo.save(newEntity);

            TokenDto dto = new TokenDto();
            dto.setToken(newEntity.getToken());
            dto.setUserName(user.getUserName());
            dto.setApiNo(tokenInDto.getApiNo());

            //대기번호 리턴
            Long count = waitTokenRepo.countPreviousToken(tokenInDto.getApiNo(),newEntity.getUpdatedAt());


            if(count == 0) {
                Long countProcss = waitTokenRepo.countStatusToken(tokenInDto.getApiNo(),WaitStatus.PROCESS);
                if(countProcss==0) {
                    newEntity.setStatusCd(WaitStatus.PROCESS);
                    waitTokenRepo.save(newEntity);
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
            Long count = waitTokenRepo.countPreviousToken(tokenInDto.getApiNo(),entity.getUpdatedAt());

            if(count==0) {

                Long countProcss = waitTokenRepo.countStatusToken(tokenInDto.getApiNo(),WaitStatus.PROCESS);
                if(countProcss==0) {
                    entity.setStatusCd(WaitStatus.PROCESS);
                    waitTokenRepo.save(entity);
                }

            }
            dto.setWaitStatus(entity.getStatusCd());
            //TODO : 엔티티 dto 매퍼 구현
            return dto;
        }

    }

}
