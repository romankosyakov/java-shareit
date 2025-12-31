package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUser(Integer id);

    UserResponseDto addNewUser(NewUserDto newUserDto);

    UserResponseDto updateUser(Integer id, UpdateUserDto updateUserDto);

    void deleteUser(Integer id);
}
