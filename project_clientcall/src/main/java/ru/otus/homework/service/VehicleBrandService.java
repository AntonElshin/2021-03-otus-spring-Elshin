package ru.otus.homework.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.homework.client.CarLoanDictClient;
import ru.otus.homework.dto.VehicleBrandResDTO;

import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Service
public class VehicleBrandService {

    private CarLoanDictClient carLoanDictClient;

    private String JWT_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiJ9._BBLSLFVxDRW7Oeg0s01_Vj-NOOp1YoTMDyGDAIHnHl3aBVZkZ32dxSyY2DClHpSgwIdHHSta-gIaMbYkqMBBA";

    public VehicleBrandService(CarLoanDictClient carLoanDictClient) {
        this.carLoanDictClient = carLoanDictClient;
    }

    public List<VehicleBrandResDTO> findAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", JWT_TOKEN);

        return carLoanDictClient.findVehicleBrandsByParams(headers, null, null).getBody();
    }

}
