package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.domain.entity.UserEntity;
import io.hhplus.concertbook.domain.entity.WalletEntity;
import io.hhplus.concertbook.domain.repository.UserRepo;
import io.hhplus.concertbook.domain.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class MoneyService {

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    UserRepo userRepo;

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
}
