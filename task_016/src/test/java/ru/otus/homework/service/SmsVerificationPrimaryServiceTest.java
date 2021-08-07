package ru.otus.homework.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;
import ru.otus.homework.Status;
import ru.otus.homework.domain.*;
import ru.otus.homework.dto.SmsVerificationCheckRequest;
import ru.otus.homework.dto.SmsVerificationCheckResponse;
import ru.otus.homework.dto.SmsVerificationPostRequest;
import ru.otus.homework.dto.SmsVerificationPostResponse;
import ru.otus.homework.repository.SmsVerificationRepository;
import ru.otus.homework.smsverificationmessage.publish.SmsVerificationMessagePublishGateway;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SmsVerificationPrimaryServiceTest {

    private static final String PHONE_NUMBER = "123456789";
    private static final String VALID_SECRET_CODE = "1111";
    private static final String INVALID_SECRET_CODE = "2222";
    private static final String GUID = UUID.randomUUID().toString();

    @MockBean
    private SmsVerificationRepository smsVerificationRepository;

    @MockBean
    private SmsVerificationMessagePublishGateway smsVerificationMessagePublishGateway;

    @Autowired
    private SmsVerificationService smsVerificationPrimaryService;

    @Before
    public void init() {

        SmsVerification smsVerification = SmsVerification.builder()
                .phoneNumber(PHONE_NUMBER)
                .secretCode(VALID_SECRET_CODE)
                .processGuid(GUID)
                .status(Status.OK.name())
                .build();

        Mockito.when(smsVerificationRepository.findBySecretCodeAndProcessGuidAndStatus(VALID_SECRET_CODE, GUID, Status.OK.name()))
                .thenReturn(Optional.of(smsVerification));
        Mockito.when(smsVerificationRepository.findBySecretCodeAndProcessGuidAndStatus(INVALID_SECRET_CODE, GUID, Status.OK.name()))
                .thenReturn(Optional.empty());


    }

    @Test
    public void dsSmsVerificationCheckTest() throws Exception {

        String secredCode = String.format("%04d", new Random().nextInt(100000));
        String guid = UUID.randomUUID().toString();

        SmsVerificationCheckRequest smsVerificationCheckRequest = SmsVerificationCheckRequest.builder()
                .code(secredCode)
                .processGUID(guid)
                .build();

        smsVerificationPrimaryService.dsSmsVerificationCheck(smsVerificationCheckRequest);

        Mockito.verify(smsVerificationRepository, Mockito.times(1)).findBySecretCodeAndProcessGuidAndStatus(
                smsVerificationCheckRequest.getCode(),
                smsVerificationCheckRequest.getProcessGUID(),
                Status.OK.name()
        );

    }

    @Test
    public void dsSmsVerificationCreate() throws Exception {

        SmsVerificationPostRequest smsVerificationPostRequest = SmsVerificationPostRequest.builder()
                .phoneNumber(PHONE_NUMBER)
                .build();

        SmsVerificationPostResponse smsVerificationPostResponse =
                smsVerificationPrimaryService.dsSmsVerificationCreate(smsVerificationPostRequest);

        Assert.assertNotNull(smsVerificationPostResponse.getProcessGUID());

    }

    @Test
    public void dsSmsVerificationCheckResultTrueTest() throws Exception {

        SmsVerificationCheckRequest smsVerificationCheckRequest = SmsVerificationCheckRequest.builder()
                .code(VALID_SECRET_CODE)
                .processGUID(GUID)
                .build();

        SmsVerificationCheckResponse smsVerificationCheckResponse =
                smsVerificationPrimaryService.dsSmsVerificationCheck(smsVerificationCheckRequest);

        Mockito.verify(smsVerificationRepository, Mockito.times(1)).findBySecretCodeAndProcessGuidAndStatus(
                VALID_SECRET_CODE,
                GUID,
                Status.OK.name()
        );

        Assert.assertTrue(smsVerificationCheckResponse.getCheckResult());

    }

    @Test
    public void dsSmsVerificationCheckResultFalseTest() throws Exception {

        SmsVerificationCheckRequest smsVerificationCheckRequest = SmsVerificationCheckRequest.builder()
                .code(INVALID_SECRET_CODE)
                .processGUID(GUID)
                .build();

        SmsVerificationCheckResponse smsVerificationCheckResponse =
                smsVerificationPrimaryService.dsSmsVerificationCheck(smsVerificationCheckRequest);

        Mockito.verify(smsVerificationRepository, Mockito.times(1)).findBySecretCodeAndProcessGuidAndStatus(
                INVALID_SECRET_CODE,
                GUID,
                Status.OK.name()
        );

        Assert.assertFalse(smsVerificationCheckResponse.getCheckResult());

    }

}
