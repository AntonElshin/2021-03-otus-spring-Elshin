package ru.otus.homework.library.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.homework.library.dto.ErrorResDTO;


@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResDTO handleBusinessException(BusinessException businessException) {
        ErrorResDTO errorResDTO = new ErrorResDTO();
        Integer code = businessException.getCode();
        errorResDTO.setCode(code.longValue());
        errorResDTO.setMessage(businessException.getMessage());
        return errorResDTO;
    }

}
