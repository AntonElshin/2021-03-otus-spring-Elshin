/*
 * Created by DQCodegen
 */
package ru.otus.homework.smsverificationmessage.publish;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

/*
spring:
  cloud:
    stream:
      bindings:
        smsVerificationMessagePublish:
          destination: smsVerificationMessage
*/
@Configuration
@EnableBinding(SmsVerificationMessagePublishChannel.class)
public class SmsVerificationMessagePublishChannelBinding {
}