package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUser(Long id);

    UserResponseDto addNewUser(NewUserDto newUserDto);

    UserResponseDto updateUser(Long id, UpdateUserDto updateUserDto);

    void deleteUser(Long id);
}
