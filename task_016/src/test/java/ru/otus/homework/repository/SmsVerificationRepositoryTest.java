package ru.otus.homework.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.homework.Status;
import ru.otus.homework.domain.SmsVerification;

import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SmsVerificationRepositoryTest {

    @Autowired
    private SmsVerificationRepository repository;

    public SmsVerification createSmsVerification() {

        SmsVerification smsVerification = SmsVerification.builder()
                .phoneNumber("9159746095")
                .processGuid(UUID.randomUUID().toString())
                .secretCode("123")
                .status(Status.WAITING.name())
                .build();

        return smsVerification;
    }

    @Test
    public void create() {
        SmsVerification smsVerification = createSmsVerification();

        SmsVerification created = repository.save(smsVerification);

        Assert.assertNotNull(created.getVerificationId());
        Assert.assertEquals(smsVerification.getPhoneNumber(), created.getPhoneNumber());
        Assert.assertEquals(smsVerification.getProcessGuid(), created.getProcessGuid());
        Assert.assertEquals(smsVerification.getSecretCode(), created.getSecretCode());
        Assert.assertEquals(smsVerification.getStatus(), created.getStatus());
    }

    @Test
    public void findBySecretcodeAndProcessguidAndStatus() {

        SmsVerification smsVerification = createSmsVerification();

        SmsVerification created = repository.save(smsVerification);

        Assert.assertNotNull(created.getVerificationId());

        Optional<SmsVerification> optional = repository.findBySecretCodeAndProcessGuidAndStatus(
                smsVerification.getSecretCode()
                , smsVerification.getProcessGuid()
                ,smsVerification.getStatus()
        );

        Assert.assertTrue(optional.isPresent());

        SmsVerification found = optional.get();

        Assert.assertEquals(created.getVerificationId(), found.getVerificationId());
        Assert.assertEquals(smsVerification.getPhoneNumber(), found.getPhoneNumber());
        Assert.assertEquals(smsVerification.getProcessGuid(), found.getProcessGuid());
        Assert.assertEquals(smsVerification.getSecretCode(), found.getSecretCode());
        Assert.assertEquals(smsVerification.getStatus(), found.getStatus());

        Assert.assertNotNull(
                repository.findBySecretCodeAndProcessGuidAndStatus(
                        smsVerification.getSecretCode()
                        , smsVerification.getProcessGuid()
                        ,smsVerification.getStatus()
                )
        );

    }

    @Test
    public void updateStatus() {

        SmsVerification smsVerification = createSmsVerification();

        SmsVerification created = repository.save(smsVerification);

        Assert.assertNotNull(created.getVerificationId());

        repository.updateStatusByProcessGuid(Status.OK.name(), created.getProcessGuid());

        Optional<SmsVerification> optional = repository.findBySecretCodeAndProcessGuidAndStatus(
                smsVerification.getSecretCode()
                ,smsVerification.getProcessGuid()
                ,Status.OK.name()
        );

        Assert.assertTrue(optional.isPresent());

        SmsVerification found = optional.get();

        Assert.assertTrue(repository.updateStatusByProcessGuid(Status.OK.name(), created.getProcessGuid()) > 0);

        Assert.assertEquals(created.getVerificationId(), found.getVerificationId());
        Assert.assertEquals(smsVerification.getPhoneNumber(), found.getPhoneNumber());
        Assert.assertEquals(smsVerification.getProcessGuid(), found.getProcessGuid());
        Assert.assertEquals(smsVerification.getSecretCode(), found.getSecretCode());
        Assert.assertEquals(Status.OK.name(), found.getStatus());

    }

}
