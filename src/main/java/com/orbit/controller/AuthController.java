package com.orbit.controller;

import com.orbit.dto.request.LoginRequest;
import com.orbit.dto.request.SignupRequest;
import com.orbit.dto.response.JwtResponse;
import com.orbit.dto.response.MessageResponse;
import com.orbit.models.auth.EnumRole;
import com.orbit.models.auth.Role;
import com.orbit.models.auth.Users;
import com.orbit.repository.RolesRepository;
import com.orbit.repository.UserRepository;
import com.orbit.security.JwtUtils;
import com.orbit.services.users.UserDetailsImpl;
import com.orbit.services.users.UserDetailsServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
//@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RolesRepository roleRepository;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private ModelMapper modelMapper;

//    @PostMapping("/addUser")
//    public ResponseEntity<ServiceResponse> addUser(@Valid @RequestBody SignupRequest signupRequest) throws Exception {
//        return ResponseEntity.ok(userService.createUser(signupRequest));
//    }

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @PostMapping("/addUser")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new user's account

        Users userInfo = modelMapper.map(signUpRequest, Users.class);
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        Set<Role> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            });
        }

        userInfo.setRoles(roles);
        userRepository.save(userInfo);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

    }

        @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new JwtResponse(userDetails.getPassword(),
                    userDetails.getUsername(), roles));
        }


    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new MessageResponse("You've been signed out!"));
    }

}
