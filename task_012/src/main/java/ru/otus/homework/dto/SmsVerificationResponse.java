package ru.otus.homework.dto;

public class SmsVerificationResponse {

    private String processGuid;

    private String secretCode;

    public SmsVerificationResponse(String processGuid, String secretCode) {
        this.processGuid = processGuid;
        this.secretCode = secretCode;
    }

    public String getProcessGuid() {
        return processGuid;
    }

    public String getSecretCode() {
        return secretCode;
    }

    @Override
    public String toString() {
        return "SmsVerificationResponse{" +
                "processGuid='" + processGuid + '\'' +
                ", secretCode='" + secretCode + '\'' +
                '}';
    }
}
