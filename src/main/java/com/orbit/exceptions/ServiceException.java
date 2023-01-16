package com.orbit.exceptions;

import lombok.Data;

@Data
public class ServiceException extends RuntimeException {
    private final Integer httpCode;
    private String timestamp;

    public ServiceException(Integer httpCode, String message, String timestamp) {
        super(message);
        this.httpCode = httpCode;
        this.timestamp = timestamp;
    }
}
