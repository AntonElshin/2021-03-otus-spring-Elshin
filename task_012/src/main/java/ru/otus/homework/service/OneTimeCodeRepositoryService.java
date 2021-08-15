package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.SmsVerification;

@Service
public class OneTimeCodeRepositoryService {

    public SmsVerification save(SmsVerification smsVerification) throws Exception {
        System.out.println("Saving to db: " + smsVerification);
        return smsVerification;
    }

}
