package com.orbit.utils;

import com.orbit.dto.response.ServiceResponse;
import com.orbit.enums.ResponseCode;

import java.time.LocalDateTime;

public class ResponseBuilder {
    public static ServiceResponse buildSuccessfulResponse(Object data) {
        return ServiceResponse.builder().statusCode(ResponseCode.OK.getCanonicalCode())
            .statusMessage(ResponseCode.OK.getDescription()).timestamp(LocalDateTime.now().toString()).data(data)
            .build();

    }
}
