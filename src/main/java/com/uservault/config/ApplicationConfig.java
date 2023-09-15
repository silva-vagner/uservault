package com.uservault.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

@Configuration
@OpenAPIDefinition
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public Principal principal() {
        return () -> SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
