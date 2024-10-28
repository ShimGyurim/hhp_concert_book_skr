package io.hhplus.concertbook.domain.service;

import io.hhplus.concertbook.common.exception.CustomException;
import io.hhplus.concertbook.common.exception.ErrorCode;
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
            throw new CustomException(ErrorCode.USER_ERROR);
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
            throw new CustomException(ErrorCode.CHARGE_INPUT_ERROR);
        }

        //금액 충전

        UserEntity user = userRepository.findByUserName(userName);

        if(user == null) {
            throw new CustomException(ErrorCode.USER_ERROR);
        }

        WalletEntity wallet = walletRepository.findByUser_UserIdWithLock(user.getUserId());
        wallet.setAmount(wallet.getAmount()+chargeAmt);
        walletRepository.save(wallet);

        return wallet.getAmount();
    }

    public WalletEntity findAndLockWallet(Long userId) throws CustomException {
        WalletEntity wallet = walletRepository.findByUser_UserIdWithLock(userId);
        if (wallet == null) {
            throw new CustomException(ErrorCode.NO_WALLET);
        }
        return wallet;
    }

    public void deductAmount(WalletEntity wallet, long amount) throws CustomException {
        if (wallet.getAmount() < amount) {
            throw new CustomException(ErrorCode.NO_BALANCE);
        }
        wallet.setAmount(wallet.getAmount() - amount);
        walletRepository.save(wallet);
    }
}
