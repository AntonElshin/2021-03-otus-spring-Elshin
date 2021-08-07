/*
 * Created by DQCodegen
 */
package ru.otus.homework.smsverificationdelivered.subscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;

import ru.otus.homework.model.SmsDeliveredMessage;


@Configuration
public class SmsVerificationDeliveredSubscribeListener {

    @Autowired
    private SmsVerificationDeliveredSubscribeListenerService listenerService;

    @StreamListener(SmsVerificationDeliveredSubscribeChannelConstants.SMS_VERIFICATION_DELIVERED)
    void smsVerificationDelivered(SmsDeliveredMessage msg) {
        listenerService.smsVerificationDelivered(msg);
    }
    
}