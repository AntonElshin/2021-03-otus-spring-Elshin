package ru.otus.homework.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.client.CarLoanDictClient;
import ru.otus.homework.props.CarLoanDictClientProperties;

@Configuration
@ConditionalOnProperty("ru.otus.homework.car-loan-dict.base-url")
@EnableConfigurationProperties(CarLoanDictClientProperties.class)
public class CarLoanDictClientAutoConfiguration {

    private CarLoanDictClientProperties props;

    public CarLoanDictClientAutoConfiguration(CarLoanDictClientProperties props) {
        this.props = props;
    }

    @Bean
    public RestTemplateBuilder carLoanDictTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    @ConditionalOnMissingBean(CarLoanDictClient.class)
    public CarLoanDictClient libraryClient(RestTemplateBuilder restTemplateBuilder) {
        return new CarLoanDictClient(restTemplateBuilder.build(), props);
    }

}

