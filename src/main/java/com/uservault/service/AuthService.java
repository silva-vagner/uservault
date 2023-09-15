package com.uservault.service;

import com.uservault.dto.auth.LoginResponseDTO;
import com.uservault.dto.auth.RegistrationDTO;
import com.uservault.enums.RoleType;
import com.uservault.model.Role;
import com.uservault.model.User;
import com.uservault.repository.RoleRepository;
import com.uservault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public User registerUser(RegistrationDTO registrationDTO, List<RoleType> roles){
        Optional<User> existingUser = userRepository.findByEmail(registrationDTO.getEmail());
        if(existingUser.isPresent()){
            return null;
        }

        String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());

        List<String> userRoles = roles.stream().map(Enum::name).toList();
        Set<Role> authorities = roleRepository.findAllByAuthorityIn(userRoles);

        User user = userRepository.save(new User(null, registrationDTO.getEmail(),
                registrationDTO.getName(), encodedPassword, authorities));
        return user;
    }

    public LoginResponseDTO loginUser(String email, String password){
        try{
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );

            return new LoginResponseDTO(tokenService.generateJwt(auth));
        }catch(AuthenticationException e){
            return null;
        }
    }
}
