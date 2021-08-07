package ru.otus.homework.smsverificationdelivered.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.otus.homework.model.SmsDeliveredMessage;
import ru.otus.homework.repository.SmsVerificationRepository;

@RequiredArgsConstructor
@Service
public class SmsVerificationDeliveredSubscribeListenerServiceImpl
        implements SmsVerificationDeliveredSubscribeListenerService {

    private final SmsVerificationRepository repository;

    @Override
    public void smsVerificationDelivered(SmsDeliveredMessage msg) {
        repository.updateStatusByProcessGuid("OK", msg.getQuid());
    }
}
