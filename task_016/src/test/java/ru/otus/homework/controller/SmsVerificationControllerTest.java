package ru.otus.homework.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.Status;
import ru.otus.homework.domain.SmsVerification;
import ru.otus.homework.repository.SmsVerificationRepository;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SmsVerificationControllerTest {

    private static final String PHONE_NUMBER = "123456789";
    private static final String VALID_SECRET_CODE = "1111";
    private static final String INVALID_SECRET_CODE = "2222";
    private static final String GUID = UUID.randomUUID().toString();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SmsVerificationRepository smsVerificationRepository;

    @Before
    public void init() {

        smsVerificationRepository.deleteAll();

    }

    @Test
    public void checkValidCodeResultTrue() throws Exception {

        smsVerificationRepository.deleteAll();

        SmsVerification smsVerification = SmsVerification.builder()
                .phoneNumber(PHONE_NUMBER)
                .secretCode(VALID_SECRET_CODE)
                .processGuid(GUID)
                .status(Status.OK.name())
                .build();

        smsVerificationRepository.save(smsVerification);

        this.mockMvc.perform(get("/v1/sms-verification?Code=" + VALID_SECRET_CODE + "&ProcessGUID=" + GUID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));

    }

    @Test
    public void checkValidCodeResultFalse() throws Exception {

        smsVerificationRepository.deleteAll();

        SmsVerification smsVerification = SmsVerification.builder()
                .phoneNumber(PHONE_NUMBER)
                .secretCode(VALID_SECRET_CODE)
                .processGuid(GUID)
                .status(Status.WAITING.name())
                .build();

        smsVerificationRepository.save(smsVerification);

        this.mockMvc.perform(get("/v1/sms-verification?Code=" + VALID_SECRET_CODE + "&ProcessGUID=" + GUID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("false")));

    }

    @Test
    public void checkInvalidCodeResultFalse() throws Exception {

        smsVerificationRepository.deleteAll();

        SmsVerification smsVerification = SmsVerification.builder()
                .phoneNumber(PHONE_NUMBER)
                .secretCode(VALID_SECRET_CODE)
                .processGuid(GUID)
                .status(Status.OK.name())
                .build();

        smsVerificationRepository.save(smsVerification);

        this.mockMvc.perform(get("/v1/sms-verification?Code=" + INVALID_SECRET_CODE + "&ProcessGUID=" + GUID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("false")));

    }

}
