package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.UserRequestDto;
import com.vvdn.ems_backend.dtos.UserResponseDto;

import com.vvdn.ems_backend.entity.Role;
import com.vvdn.ems_backend.entity.User;

import com.vvdn.ems_backend.repository.UserRepository;
import com.vvdn.ems_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UUID getEmployeeId(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEmployee() == null) {
            throw new RuntimeException("Employee not mapped to user");
        }

        return user.getEmployee().getEmpId();
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {

//        Employee employee = employeeRepository.findById(dto.getEmpId())
//                .orElseThrow(() -> new RuntimeException("Employee not found"));

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.valueOf(dto.getRole()))
               // .employee(employee)
                .isActive(dto.isActive())
                .build();

        return mapToDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(UUID userId, UserRequestDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getUsername() != null)
            user.setUsername(dto.getUsername());

        if (dto.getPassword() != null)
            user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getRole() != null)
            user.setRole(Role.valueOf(dto.getRole()));

//        if (dto.getEmpId() != null) {
//            Employee employee = employeeRepository.findById(dto.getEmpId())
//                    .orElseThrow(() -> new RuntimeException("Employee not found"));
//            user.setEmployee(employee);
//        }

        user.setActive(dto.isActive());

        return mapToDto(userRepository.save(user));
    }


    @Override
    public void activateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    private UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                //.empId(user.getEmployee() != null ? user.getEmployee().getEmpId() : null)
                .isActive(user.isActive())
                .build();
    }
}
