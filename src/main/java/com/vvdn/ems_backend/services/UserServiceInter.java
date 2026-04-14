package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.UserDto;

public interface UserServiceInter {

    Iterable<UserDto> getAllUsers();
}
