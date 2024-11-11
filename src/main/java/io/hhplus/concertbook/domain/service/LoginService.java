package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;

    public boolean login(String userLoginId,String password) {
        if(userLoginId == null)
            throw new CustomException(ErrorCode.USER_ERROR);

        UserEntity user = userRepository.findByUserLoginId(userLoginId);
        String dbpw = user != null ? user.getPassword() : null;

        if(dbpw != null) {
            if(userLoginId.equals(user.getUserLoginId()) && password.equals(user.getPassword()))
                return true;
            else
                return false;
        }else {
            if(userLoginId.equals(user.getUserLoginId()))
                return true;
            else
                return false;
        }


    }
}
