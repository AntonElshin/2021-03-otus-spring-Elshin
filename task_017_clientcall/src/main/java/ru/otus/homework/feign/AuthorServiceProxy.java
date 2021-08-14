package ru.otus.homework.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.homework.library.dto.AuthorResListDTO;

import java.util.List;

@FeignClient(name = "author-service", url = "http://localhost:8080/author/authors")
public interface AuthorServiceProxy {

    @GetMapping(value = "/all")
    List<AuthorResListDTO> getAllAuthors(@RequestHeader(value = "Authorization", required = true) String authorizationHeader);

}
