package ru.otus.homework.subscriber;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.model.SmsDeliveredMessage;
import ru.otus.homework.model.SmsVerificationMessage;
import ru.otus.homework.publisher.SmsVerificationDeliveredPublishGateway;

@RequiredArgsConstructor
@Configuration
public class SmsVerificationMessageListener {

    private final SmsVerificationDeliveredPublishGateway gateway;

    @StreamListener(ConsumerChannels.SMS_VERIFICATION_MESSAGE)
    public void smsVerificationMessage(SmsVerificationMessage smsVerification) throws InterruptedException {
        Thread.sleep(3000);
        gateway.smsVerificationDelivered(SmsDeliveredMessage.builder().quid(smsVerification.getQuid()).build());
    }

}
