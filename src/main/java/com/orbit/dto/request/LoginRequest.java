package com.orbit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class LoginRequest {
   @NotBlank
    private String userName;
   @NotBlank
    private String password;
}
