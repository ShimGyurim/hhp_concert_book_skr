package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.repository.UserRepository;
import jakarta.persistence.Access;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;

    public boolean login(String userName,String password) {
        if(userName == null)
            throw new CustomException(ErrorCode.USER_ERROR);

        UserEntity user = userRepository.findByUserName(userName);
        String dbpw = user != null ? user.getPassword() : null;

        if(dbpw != null) {
            if(userName.equals(user.getUserName()) && password.equals(user.getPassword()))
                return true;
            else
                return false;
        }else {
            if(userName.equals(user.getUserName()))
                return true;
            else
                return false;
        }


    }
}
