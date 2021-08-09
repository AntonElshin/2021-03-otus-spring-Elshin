package ru.otus.homework.publisher;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import ru.otus.homework.model.SmsDeliveredMessage;

@MessagingGateway
public interface SmsVerificationDeliveredPublishGateway {

    @Gateway(requestChannel = ProducerChannels.SMS_VERIFICATION_DELIVERED)
    void smsVerificationDelivered(SmsDeliveredMessage msg);

}
