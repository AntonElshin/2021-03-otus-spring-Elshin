/*
 * Created by DQCodegen
 */
package ru.otus.homework.service;

import ru.otus.homework.dto.SmsVerificationCheckRequest;
import ru.otus.homework.dto.SmsVerificationCheckResponse;
import ru.otus.homework.dto.SmsVerificationPostRequest;
import ru.otus.homework.dto.SmsVerificationPostResponse;

public interface SmsVerificationService {

    SmsVerificationCheckResponse dsSmsVerificationCheck(
        SmsVerificationCheckRequest smsVerificationCheckRequest);
    
    SmsVerificationPostResponse dsSmsVerificationCreate(
        SmsVerificationPostRequest smsVerificationPostRequest);
    
}
