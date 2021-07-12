package ru.otus.homework.library.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.library.client.LibraryClient;
import ru.otus.homework.library.props.LibraryClientProperties;

@Configuration
@ConditionalOnProperty("ru.otus.homework.library.base-url")
@EnableConfigurationProperties(ru.otus.homework.library.props.LibraryClientProperties.class)
public class LibraryClientAutoConfiguration {

    private LibraryClientProperties props;

    public LibraryClientAutoConfiguration(LibraryClientProperties props) {
        this.props = props;
    }

    @Bean
    public RestTemplateBuilder carLoanDictTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    @ConditionalOnMissingBean(LibraryClient.class)
    public LibraryClient libraryClient(RestTemplateBuilder restTemplateBuilder) {
        return new LibraryClient(restTemplateBuilder.build(), props);
    }

}

