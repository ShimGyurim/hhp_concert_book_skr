package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
import io.hhplus.concertbook.domain.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public void validateUser(UserEntity userBook, UserEntity userToken) throws CustomException {
        if (userBook == null || userToken == null) {
            throw new CustomException(ErrorCode.USER_ERROR);
        } else if (userBook.getUserId() != userToken.getUserId()) {
            throw new CustomException(ErrorCode.NO_AUTH);
        }
    }

}
