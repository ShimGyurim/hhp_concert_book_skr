package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.exception.NoUserException;
import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WalletEntity;
import io.hhplus.concertbook.domain.repository.UserRepository;
import io.hhplus.concertbook.domain.repository.WaitTokenRepository;
import io.hhplus.concertbook.domain.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoneyService {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WaitTokenRepository waitTokenRepository;

    @Autowired
    WaitQueueService waitQueueService;

    public long getBalance(String userName) throws Exception {

        UserEntity user = userRepository.findByUserName(userName);


        if(user == null) {
            throw new NoUserException("유저정보없음");
        }

        WalletEntity wallet = walletRepository.findByUser_UserId(user.getUserId());
        if(wallet == null) {
            WalletEntity entity = new WalletEntity();
            entity.setAmount(0);
            entity.setUser(user);

            walletRepository.save(entity);
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

        UserEntity user = userRepository.findByUserName(userName);

        if(user == null) {
            throw new Exception("유저 없음");
        }

        WalletEntity wallet = walletRepository.findByUser_UserId(user.getUserId());
        wallet.setAmount(wallet.getAmount()+chargeAmt);
        walletRepository.save(wallet);

        return wallet.getAmount();
    }
}
