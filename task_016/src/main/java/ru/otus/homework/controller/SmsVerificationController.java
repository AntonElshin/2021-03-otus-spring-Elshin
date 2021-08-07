/*
 * Created by DQCodegen
 */
package ru.otus.homework.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.dto.SmsVerificationCheckRequest;
import ru.otus.homework.dto.SmsVerificationCheckResponse;
import ru.otus.homework.dto.SmsVerificationPostRequest;
import ru.otus.homework.dto.SmsVerificationPostResponse;
import ru.otus.homework.service.SmsVerificationService;

@RestController("ru.otus.homework.controller.SmsVerificationController")
@Api(tags = {"Demo"})
public class SmsVerificationController {

    private final SmsVerificationService smsVerificationService;
    
    @Autowired    
    public SmsVerificationController(SmsVerificationService smsVerificationService) {
        this.smsVerificationService = smsVerificationService;
    }

    @GetMapping("/v1/sms-verification")
    @ApiOperation(value = "Метод проверяет, что введенный код соответствует отправленному.", response = SmsVerificationCheckResponse.class, tags = {"SmsVerification"})
    public ResponseEntity<SmsVerificationCheckResponse> dsSmsVerificationCheck(
            @RequestParam(name = "ProcessGUID") String processGUID,
            @RequestParam(name = "Code") String code
    ) {
        SmsVerificationCheckRequest request = new SmsVerificationCheckRequest(processGUID, code);

        SmsVerificationCheckResponse result = smsVerificationService.dsSmsVerificationCheck(
                request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/sms-verification")
    @ApiOperation(value = "Отправка проверочного кода на телефон клиента.", response = SmsVerificationPostResponse.class, tags = {"SmsVerification"})
    public ResponseEntity<SmsVerificationPostResponse> dsSmsVerificationCreate(
                @RequestBody
                @ApiParam(name = "SmsVerificationPostRequest", value = "", required = false)
                        SmsVerificationPostRequest smsVerificationPostRequest) {
        SmsVerificationPostResponse result = smsVerificationService.dsSmsVerificationCreate(
                smsVerificationPostRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
