/*
 * Created by DQCodegen
 */
package ru.otus.homework.smsverificationmessage.publish;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/*
spring:
  cloud:
    stream:
      bindings:
        smsVerificationMessagePublish:
          destination: smsVerificationMessage
*/
public interface SmsVerificationMessagePublishChannel {

    @Output(SmsVerificationMessagePublishChannelConstants.SMS_VERIFICATION_MESSAGE)
    MessageChannel smsVerificationMessage();
    
}