package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.repository.UserRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;

    public boolean login(String userName) {
        if(userName == null)
            throw new CustomException(ErrorCode.USER_ERROR);
        UserEntity user = userRepository.findByUserName(userName);
        if(userName.equals(user.getUserName()))
            return true;
        else
            return false;
    }
}
