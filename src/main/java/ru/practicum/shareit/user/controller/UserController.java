package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        log.debug("Вызван метод получения списка всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable("id") Integer id) {
        log.debug("Вызван метод получения пользователя с ID: {}", id);
        return userService.getUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto addNewUser(@Validated(CreateValidation.class) @Valid @RequestBody NewUserDto newUserDto) {
        log.debug("Вызван метод добавления пользователя");
        return userService.addNewUser(newUserDto);
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@Validated(UpdateValidation.class) @PathVariable("id") Integer id,
                                      @RequestBody UpdateUserDto updateUserDto) {
        log.debug("Вызван метод обновления пользователя с ID: {}", id);
        return userService.updateUser(id, updateUserDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Integer id) {
        log.debug("Вызван метод удаления пользователя");
        userService.deleteUser(id);
    }
}
