package com.vvdn.ems_backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class ProjectConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
