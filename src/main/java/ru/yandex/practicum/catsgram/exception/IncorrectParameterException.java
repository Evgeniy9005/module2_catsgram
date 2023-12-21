package ru.yandex.practicum.catsgram.exception;

public class IncorrectParameterException extends RuntimeException {
    private String parameter;
    public IncorrectParameterException(String parameter) {
        super(parameter);
        this.parameter = parameter;
    }

}
