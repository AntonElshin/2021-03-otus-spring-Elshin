package ru.otus.homework.library.props;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("ru.otus.homework.library")
@ConstructorBinding
@RequiredArgsConstructor
public class LibraryClientProperties {
    public final String baseUrl;
}
