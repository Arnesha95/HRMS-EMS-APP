package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.UserDto;
import com.vvdn.ems_backend.repository.UserLogInRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserLogInRepo userLogInRepo;
    private final ModelMapper modelMapper;


    @Transactional
    public Iterable<UserDto> getAllUsers(){
        return userLogInRepo
                .findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
