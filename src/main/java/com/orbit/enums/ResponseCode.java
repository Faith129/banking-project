package com.orbit.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;
@Getter
public enum ResponseCode {

    OK("0", "0000", "Success", HttpStatus.OK),
    INVALID_CREDENTIALS("1", "1000", "Username or Password Incorrect", HttpStatus.BAD_REQUEST),
    ALREADY_EXIST("137", "4001", "Account already exist", HttpStatus.FORBIDDEN),
    NOT_FOUND("139", "3004", "Account not found", HttpStatus.NOT_FOUND),
    BAD_REQUEST("400", "4000", "Invalid request", HttpStatus.BAD_REQUEST), INTERNAL_SERVER_ERROR("500", "3001",
        "There was an error while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);

    ResponseCode(final String errorCode, final String canonicalCode, final String description,
                 final HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.canonicalCode = canonicalCode;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    private String errorCode;
    private String canonicalCode;
    private String description;
    private HttpStatus httpStatus;

    public static Optional<ResponseCode> getResponseCode(String code) {
        for (ResponseCode canonicalErrorCode : ResponseCode.values()) {
            if (canonicalErrorCode.errorCode.equals(code)) {
                return Optional.of(canonicalErrorCode);
            }
        }
        return Optional.empty();
    }
}
