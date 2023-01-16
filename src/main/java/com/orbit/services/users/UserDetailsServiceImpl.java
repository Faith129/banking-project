package com.orbit.services.users;

import com.orbit.dto.request.SignupRequest;
import com.orbit.dto.response.ServiceResponse;
import com.orbit.models.auth.Users;
import com.orbit.repository.UserRepository;
import com.orbit.services.users.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.orbit.enums.ResponseCode.INTERNAL_SERVER_ERROR;
import static com.orbit.enums.ResponseCode.OK;
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRep;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users user = userRep.findByUserName(userName)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userName));

        return UserDetailsImpl.build(user);
    }
    public ServiceResponse createUser(SignupRequest dto) throws Exception {

        Users user = modelMapper.map(dto, Users.class);
        user.setPassword(bcryptEncoder.encode(dto.getPassword()));

        ServiceResponse response;
        try {
            response = new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
                userRep.save(user));
        } catch (Exception e) {
            log.error("Exception occurred while creating user {}", e.getMessage());
            return new ServiceResponse(INTERNAL_SERVER_ERROR.getCanonicalCode(), INTERNAL_SERVER_ERROR.getDescription(),
                LocalDateTime.now().toString(), null);
        }
        return response;
    }
}
