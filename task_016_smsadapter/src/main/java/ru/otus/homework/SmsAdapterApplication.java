package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.IntegrationComponentScan;
import ru.otus.homework.publisher.ProducerChannels;
import ru.otus.homework.subscriber.ConsumerChannels;

@SpringBootApplication
@EnableBinding({ConsumerChannels.class, ProducerChannels.class})
@IntegrationComponentScan
public class SmsAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsAdapterApplication.class, args);
    }

}
