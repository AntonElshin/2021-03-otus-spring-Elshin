/*
 * Created by DQCodegen
 */
package ru.otus.homework.smsverificationdelivered.subscribe;


import ru.otus.homework.model.SmsDeliveredMessage;


public interface SmsVerificationDeliveredSubscribeListenerService {

    void smsVerificationDelivered(SmsDeliveredMessage msg);
    
}