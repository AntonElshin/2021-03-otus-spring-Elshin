package ru.otus.homework.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.homework.dto.ErrorDTO;


@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBusinessException(BusinessException businessException) {
        ErrorDTO errorDTO = new ErrorDTO();
        Integer code = businessException.getCode();
        errorDTO.setCode(code.longValue());
        errorDTO.setMessage(businessException.getMessage());
        return errorDTO;
    }

}
