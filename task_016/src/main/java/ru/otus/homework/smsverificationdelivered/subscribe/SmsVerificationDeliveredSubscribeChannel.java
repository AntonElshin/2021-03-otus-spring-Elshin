/*
 * Created by DQCodegen
 */
package ru.otus.homework.smsverificationdelivered.subscribe;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
/*
spring:
  cloud:
    stream:
      bindings:
        smsVerificationDeliveredSubscribe:
          destination: sms-verification-delivered
*/
public interface SmsVerificationDeliveredSubscribeChannel {

    @Input(SmsVerificationDeliveredSubscribeChannelConstants.SMS_VERIFICATION_DELIVERED)
    SubscribableChannel smsVerificationDelivered();
    
}