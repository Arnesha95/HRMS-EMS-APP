package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.UserRequestDto;
import com.vvdn.ems_backend.dtos.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDto createUser(UserRequestDto dto);

    UserResponseDto getUserById(UUID userId);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(UUID userId, UserRequestDto dto);

    void activateUser(UUID id);

    void deactivateUser(UUID id);

    void deleteUser(UUID userId);

    UUID getEmployeeId(UUID userId);


}
