/*
 * Created by DQCodegen
 */
package ru.otus.homework.smsverificationdelivered.subscribe;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

/*
spring:
  cloud:
    stream:
      bindings:
        smsVerificationDeliveredSubscribe:
          destination: sms-verification-delivered
*/
@Configuration
@EnableBinding(SmsVerificationDeliveredSubscribeChannel.class)
public class SmsVerificationDeliveredSubscribeChannelBinding {
}