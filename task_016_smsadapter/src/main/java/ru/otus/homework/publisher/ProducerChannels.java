package ru.otus.homework.publisher;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ProducerChannels {

    String SMS_VERIFICATION_DELIVERED = "smsVerificationDeliveredPublish";

    @Output(SMS_VERIFICATION_DELIVERED)
    MessageChannel smsVerificationDeliveredPublish();
    
}
