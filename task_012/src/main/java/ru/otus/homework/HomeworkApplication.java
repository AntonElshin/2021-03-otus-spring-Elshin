package ru.otus.homework;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.homework.domain.SmsVerification;
import ru.otus.homework.dto.SmsVerificationResponse;


@IntegrationComponentScan
@ComponentScan
@Configuration
@EnableIntegration
public class HomeworkApplication {
    private static final String[] PHONES = { "89858698107", "89858698108", "89858698109", "89858698110", "89858698111", "89858698112", "89858698113" };

    @Bean
    public QueueChannel oneTimeCodeRequestChannel() {
        return MessageChannels.queue( 10 ).get();
    }

    @Bean
    public PublishSubscribeChannel oneTimeCodeResponseChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate( 100 ).maxMessagesPerPoll( 2 ).get();
    }

    @Bean
    public IntegrationFlow smsCodeGenerationFlow() {
        return IntegrationFlows.from( "oneTimeCodeRequestChannel" )
                .transform(o -> new SmsVerification(o.toString()))
                .handle( "oneTimeCodeGenerationService", "generateGuidAndCode" )
                .handle( "oneTimeCodeRepositoryService", "save" )
                .handle( "oneTimeCodeSenderService", "send" )
                .channel( "oneTimeCodeResponseChannel" )
                .get();
    }

    public static void main(String[] args) throws Exception {
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(HomeworkApplication.class);

        OneTimeCodeManager OneTimeCodeManager = ctx.getBean(OneTimeCodeManager.class);

        while (true) {
            Thread.sleep(5000);

            String phoneNumber = getCustomPhoneNumber();
            System.out.println("Phone number: " + phoneNumber);
            SmsVerificationResponse smsVerificationResponse = OneTimeCodeManager.process(phoneNumber);
            System.out.println("Sending " + smsVerificationResponse);
        }
    }

    private static String getCustomPhoneNumber() {
        return PHONES[RandomUtils.nextInt(0, PHONES.length)];
    }
}
