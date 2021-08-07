package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.otus.homework.Status;
import ru.otus.homework.domain.*;
import ru.otus.homework.dto.SmsVerificationCheckRequest;
import ru.otus.homework.dto.SmsVerificationCheckResponse;
import ru.otus.homework.dto.SmsVerificationPostRequest;
import ru.otus.homework.dto.SmsVerificationPostResponse;
import ru.otus.homework.model.SmsVerificationMessage;
import ru.otus.homework.smsverificationmessage.publish.SmsVerificationMessagePublishGateway;
import ru.otus.homework.repository.SmsVerificationRepository;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SmsVerificationServiceImpl implements SmsVerificationService {

    private final SmsVerificationRepository repository;
    private final SmsVerificationMessagePublishGateway publishGateway;

    @Override
    public SmsVerificationCheckResponse dsSmsVerificationCheck(SmsVerificationCheckRequest smsVerificationCheckRequest) {
        Optional<SmsVerification> verification = repository.findBySecretCodeAndProcessGuidAndStatus(
                smsVerificationCheckRequest.getCode()
                ,smsVerificationCheckRequest.getProcessGUID()
                , Status.OK.name()
        );

        return new SmsVerificationCheckResponse(verification.isPresent());
    }

    @Override
    public SmsVerificationPostResponse dsSmsVerificationCreate(SmsVerificationPostRequest smsVerificationPostRequest) {

        String guid = UUID.randomUUID().toString();
        String secredCode = String.format("%04d", new Random().nextInt(100000));
        SmsVerification smsVerification = SmsVerification.builder()
                .phoneNumber(smsVerificationPostRequest.getPhoneNumber())
                .processGuid(guid)
                .secretCode(secredCode)
                .status(Status.WAITING.name()).build();

        repository.save(smsVerification);

        publishGateway.smsVerificationMessage(SmsVerificationMessage.builder().quid(smsVerification.getProcessGuid()).build());

        return new SmsVerificationPostResponse(guid);

    }


}
