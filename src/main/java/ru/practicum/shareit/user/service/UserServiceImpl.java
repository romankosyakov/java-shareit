package ru.practicum.shareit.user.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Data
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userStorage.getAllUsers()
                .stream()
                .map(UserMapper::toResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto getUser(Integer id) {
        return userStorage.getUser(id)
                .map(UserMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Пользователь c id: " + id + " не найден"));
    }

    @Override
    public UserResponseDto addNewUser(NewUserDto newUserDto) {
        User user = UserMapper.toEntity(newUserDto);

        if (user.getEmail() == null) {
            throw new ValidationException("Email обязателен для заполнения.");
        }
        boolean isNotAlreadySaved = userStorage.findByEmail(user.getEmail()).isEmpty();

        if (!isNotAlreadySaved) {
            throw new ConflictException("Пользователь с таким email уже существует.");
        }

        return UserMapper.toResponseDto(userStorage.addNew(user));
    }

    @Override
    public UserResponseDto updateUser(Integer id, UpdateUserDto updateUserDto) {  // Изменить сигнатуру
        User user = userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));

        // Проверка уникальности email
        if (updateUserDto.getEmail() != null && !updateUserDto.getEmail().equals(user.getEmail())) {
            if (userStorage.existsByEmail(updateUserDto.getEmail())) {
                throw new ConflictException("Email уже используется другим пользователем");
            }
        }

        UserMapper.updateEntityFromDto(updateUserDto, user);

        userStorage.update(user);

        log.info("Обновлен пользователь: '{}' (ID: {})", user.getName(), user.getId());
        return UserMapper.toResponseDto(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userStorage.delete(id);
    }
}