package ru.otus.homework.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.homework.library.client.LibraryClient;
import ru.otus.homework.library.dto.GenreResListDTO;

import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Service
public class GenreService {

    private LibraryClient libraryClient;

    private String JWT_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiJ9._BBLSLFVxDRW7Oeg0s01_Vj-NOOp1YoTMDyGDAIHnHl3aBVZkZ32dxSyY2DClHpSgwIdHHSta-gIaMbYkqMBBA";

    public GenreService(LibraryClient libraryClient) {
        this.libraryClient = libraryClient;
    }

    public List<GenreResListDTO> findAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", JWT_TOKEN);

        return libraryClient.getAllGenres(headers).getBody();
    }

}
