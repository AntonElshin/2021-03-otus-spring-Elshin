package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.SmsVerification;
import ru.otus.homework.dto.SmsVerificationResponse;

import java.util.Random;
import java.util.UUID;

@Service
public class OneTimeCodeGenerationService {

    public SmsVerification generateGuidAndCode(SmsVerification smsVerification) throws Exception {
        System.out.println("Generate guid and code for phone number: " + smsVerification.getPhoneNumber());

        Thread.sleep(3000);
        String guid = UUID.randomUUID().toString();
        String secredCode = String.format("%04d", new Random().nextInt(100000));

        smsVerification.setProcessGuid(guid);
        smsVerification.setSecretCode(secredCode);

        return smsVerification;
    }

}
