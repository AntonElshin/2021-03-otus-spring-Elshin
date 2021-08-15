package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.feign.AuthorServiceProxy;
import ru.otus.homework.library.dto.AuthorResListDTO;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorServiceProxy authorServiceProxy;

    private String JWT_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiJ9._BBLSLFVxDRW7Oeg0s01_Vj-NOOp1YoTMDyGDAIHnHl3aBVZkZ32dxSyY2DClHpSgwIdHHSta-gIaMbYkqMBBA";

    public List<AuthorResListDTO> findAll() {
        return authorServiceProxy.getAllAuthors(JWT_TOKEN);
    }

}
