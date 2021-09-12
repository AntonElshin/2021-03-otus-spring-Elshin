package ru.otus.homework.props;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("ru.otus.homework.car-loan-dict")
@ConstructorBinding
@RequiredArgsConstructor
public class CarLoanDictClientProperties {
    public final String baseUrl;
}
