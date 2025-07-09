package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class BalanceController {

    @Autowired
    private UserRecordRepository userRepository;

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam("userId") Long userId) {
        Optional<UserRecord> user = userRepository.findById(userId);
        float balance = user.map(UserRecord::getBalance).orElse(0f);
        return new Balance(balance);
    }
}
