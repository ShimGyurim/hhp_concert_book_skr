package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.enumerate.ApiNo;
import io.hhplus.concertbook.common.enumerate.WaitStatus;
import io.hhplus.concertbook.common.exception.NoTokenException;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WaitTokenEntity;
import io.hhplus.concertbook.domain.entity.WalletEntity;
import io.hhplus.concertbook.domain.repository.UserRepo;
import io.hhplus.concertbook.domain.repository.WaitTokenRepo;
import io.hhplus.concertbook.domain.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoneyService {

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    WaitTokenRepo waitTokenRepo;

    @Autowired
    WaitQueueService waitQueueService;

    public long getBalance(String userName) throws Exception {

        UserEntity user = userRepo.findByUserName(userName);
        WalletEntity wallet = walletRepo.findByUser_UserId(user.getUserId());

        if(user == null) {
            throw new Exception("유저정보없음");
        }
        if(wallet == null) {
            WalletEntity entity = new WalletEntity();
            entity.setAmount(0);
            entity.setUser(user);

            walletRepo.save(entity);
            return 0L;
        }
        return wallet.getAmount();
    }

    @Transactional
    public long charge(String userName,Long chargeAmt) throws Exception {
        if(chargeAmt <= 0) {
            throw new Exception("충전금액 이상");
        }

        //금액 충전

        UserEntity user = userRepo.findByUserName(userName);

        if(user == null) {
            throw new Exception("유저 없음");
        }

        WalletEntity wallet = walletRepo.findByUser_UserId(user.getUserId());
        wallet.setAmount(wallet.getAmount()+chargeAmt);
        walletRepo.save(wallet);

        return wallet.getAmount();
    }
}
