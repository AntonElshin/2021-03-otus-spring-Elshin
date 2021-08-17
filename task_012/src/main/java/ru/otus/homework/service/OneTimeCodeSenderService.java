package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.SmsVerification;
import ru.otus.homework.dto.SmsVerificationResponse;

@Service
public class OneTimeCodeSenderService {

    public SmsVerificationResponse send(SmsVerification smsVerification) throws Exception {
        System.out.println("Sending code to number: " + smsVerification.getPhoneNumber());
        return new SmsVerificationResponse(smsVerification.getProcessGuid(), smsVerification.getSecretCode());
    }

}
