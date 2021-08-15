package ru.otus.homework;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.homework.domain.SmsVerification;
import ru.otus.homework.dto.SmsVerificationResponse;

@MessagingGateway
public interface OneTimeCodeManager {

    @Gateway(requestChannel = "oneTimeCodeRequestChannel", replyChannel = "oneTimeCodeResponseChannel")
    SmsVerificationResponse process(String phoneNumber);
}
