package com.orbit.dto.request;

import com.orbit.models.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
  //  @NotBlank
    private String userName;
   // @NotBlank
    private String password;
    private Set<Role> roles = new HashSet<>();
}
