package com.xyz.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BadRequestException.class, HttpMessageNotReadableException.class})
    public void badRequestException() {
        LOGGER.info("\n\n\n\t\tgot ya!\n\n");
    }
}
