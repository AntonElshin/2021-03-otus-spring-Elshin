package ru.otus.homework.domain;

public class SmsVerification {

    private String processGuid;

    private String phoneNumber;

    private String secretCode;

    public SmsVerification(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProcessGuid(String processGuid) {
        this.processGuid = processGuid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public String getProcessGuid() {
        return processGuid;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    @Override
    public String toString() {
        return "SmsGeneration{" +
                "processGuid='" + processGuid + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", secretCode='" + secretCode + '\'' +
                '}';
    }
}
