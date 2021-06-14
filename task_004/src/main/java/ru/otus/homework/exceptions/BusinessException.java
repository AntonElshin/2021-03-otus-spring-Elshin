package ru.otus.homework.exceptions;

public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Errors error, Object... args) {
        super(String.format(error.getMessage(), args));
        this.code = error.getCode();
    }

    public int getCode() {
        return code;
    }
}
