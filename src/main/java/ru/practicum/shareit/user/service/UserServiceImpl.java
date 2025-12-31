package ru.practicum.shareit.user.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Objects;

@Data
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public List<UserResponseDto> getAllUsers() {
        return userStorage.getAllUsers()
                .stream()
                .map(UserMapper::toResponseDto)
                .toList();
    }

    public UserResponseDto getUser(Integer id) {
        return userStorage.getUser(id)
                .map(UserMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Пользователь c id: " + id + " не найден"));
    }

    public UserResponseDto addNewUser(NewUserDto newUserDto) {
        return UserMapper.toResponseDto(userStorage.addNew(UserMapper.toEntity(newUserDto)));
    }

    public UserResponseDto updateUser(UpdateUserDto updateUserDto, Integer id) {

        if (userStorage.existsByEmail(updateUserDto.getEmail())
                && !Objects.equals(userStorage.findByEmail(updateUserDto.getEmail()).get().getId(), id)) {
            throw new ValidationException("e-mail уже используется");
        }

        User user = userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        UserMapper.updateEntityFromDto(updateUserDto, user);

        log.info("Обновлен пользователь: '{}' (ID: {})", user.getName(), user.getId());

        return UserMapper.toResponseDto(user);
    }

    public void deleteUser(Integer id) {
        userStorage.delete(id);
    }
}