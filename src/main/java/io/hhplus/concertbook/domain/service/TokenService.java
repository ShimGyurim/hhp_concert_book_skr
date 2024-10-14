package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.domain.dto.TokenDto;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import io.hhplus.concertbook.domain.repository.UserRepo;
import io.hhplus.concertbook.domain.repository.WaitTokenRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Service
public class TokenService {

    @Autowired
    WaitTokenRepo waitTokenRepo;

    @Autowired
    UserRepo userRepo;

    public TokenDto getToken(TokenDto tokenInDto) {
        // 토큰을 받아 유효한게 있는지 확인
        // api 별로는 아예 신경 안스기
        WaitTokenEntity entity = waitTokenRepo.findByUserNameAndServiceCd(tokenInDto.getUserName(),tokenInDto.getApiNo());

        if(entity == null || WaitStatus.EXPIRED.equals(entity.getStatusCd())) {
            WaitTokenEntity newEntity = new WaitTokenEntity();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String formDate = sdf.format(timestamp);

            UserEntity user = userRepo.findByUserName(tokenInDto.getUserName());
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
            return dto;

        }else{ //유효한 토큰이 존재하는 경우
            TokenDto dto = new TokenDto();
            dto.setToken(entity.getToken());
            dto.setUserName(entity.getUser().getUserName());
            dto.setApiNo(tokenInDto.getApiNo());

            return dto;
        }

    }
}
