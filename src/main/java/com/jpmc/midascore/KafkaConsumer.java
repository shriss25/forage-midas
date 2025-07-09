package com.jpmc.midascore;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class KafkaConsumer {

    @Autowired
    private UserRecordRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "group_id")
    public void consume(Transaction transaction) {
        System.out.println("⚡ Received Transaction → " + transaction);

        Optional<UserRecord> senderOpt = userRepository.findById(transaction.getSenderId());
        Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            System.out.println("❌ Invalid sender or recipient. Skipping transaction.");
            return;
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        float amount = (float) transaction.getAmount();

        if (sender.getBalance() < amount) {
            System.out.println("❌ Insufficient balance. Skipping transaction.");
            return;
        }

        // 🧠 Fetch incentive amount from external API
        float incentiveAmount = 0f;
        try {
            IncentiveResponse response = restTemplate.postForObject(
                    "http://localhost:8080/incentive",
                    transaction,
                    IncentiveResponse.class
            );
            if (response != null) {
                incentiveAmount = response.getAmount();
            }
        } catch (Exception e) {
            System.out.println("⚠️ Failed to fetch incentive: " + e.getMessage());
        }

        // Update balances
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount + incentiveAmount);

        // Save updated users
        userRepository.save(sender);
        userRepository.save(recipient);

        // Save transaction with incentive
        TransactionRecord record = new TransactionRecord(sender, recipient, amount, incentiveAmount);
        transactionRepository.save(record);

        System.out.println("✅ Transaction recorded: " + amount + " (+ incentive: " + incentiveAmount + ")");

        // Optional: Print balances
        userRepository.findAll().forEach(user -> {
            if (user.getName().equalsIgnoreCase("wilbur")) {
                System.out.println("💰 Wilbur's Final Balance: " + user.getBalance());
            }
        });
    }

    // ✅ Inner static class to parse incentive API response
    static class IncentiveResponse {
        private float amount;

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }
    }
}
