/*
 * Created by DQCodegen
 */
package ru.otus.homework.smsverificationmessage.publish;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import ru.otus.homework.model.SmsVerificationMessage;


@MessagingGateway
public interface SmsVerificationMessagePublishGateway {

    @Gateway(requestChannel = SmsVerificationMessagePublishChannelConstants.SMS_VERIFICATION_MESSAGE)
    void smsVerificationMessage(SmsVerificationMessage msg);

}

