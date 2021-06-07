package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.exceptions.Errors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    private static final ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
    private final static Validator validator = vf.getValidator();

    public void validateDTO(Object object) throws BusinessException {
        Set<ConstraintViolation<Object>> validateSet = validator.validate(object);
        if (!CollectionUtils.isEmpty(validateSet)) {
            throw new BusinessException(Errors.REQUIRED_PARAM, validateSet.stream()
                    .map(cv -> cv.getPropertyPath().toString())
                    .collect(Collectors.joining(", ")));
        }
    }

}
