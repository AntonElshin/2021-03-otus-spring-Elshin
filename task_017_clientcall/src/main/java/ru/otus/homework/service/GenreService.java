package ru.otus.homework.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.homework.library.client.LibraryClient;
import ru.otus.homework.library.dto.GenreResListDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Service
public class GenreService {

    private LibraryClient libraryClient;

    private String JWT_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiJ9._BBLSLFVxDRW7Oeg0s01_Vj-NOOp1YoTMDyGDAIHnHl3aBVZkZ32dxSyY2DClHpSgwIdHHSta-gIaMbYkqMBBA";

    public GenreService(LibraryClient libraryClient) {
        this.libraryClient = libraryClient;
    }

    @HystrixCommand(commandKey="getAllGenres", fallbackMethod="buildFallbackFindAll")
    public List<GenreResListDTO> findAll() {
        sleepRandomly();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", JWT_TOKEN);

        return libraryClient.getAllGenres(headers).getBody();
    }

    public List<GenreResListDTO> buildFallbackFindAll() {
        return new ArrayList<>();
    }

    private void sleepRandomly() {
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        if(randomNum == 3) {
            System.out.println("It is a chance for demonstrating Hystrix action");
            try {
                System.out.println("Start sleeping...." + System.currentTimeMillis());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Hystrix thread interupted...." + System.currentTimeMillis());
                e.printStackTrace();
            }
        }
    }

}
