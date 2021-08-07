package ru.otus.homework.subscriber;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ConsumerChannels {

	String SMS_VERIFICATION_MESSAGE = "smsVerificationMessageSubscribe";

    @Input(SMS_VERIFICATION_MESSAGE)
    SubscribableChannel smsVerificationMessageSubscribe();

}
