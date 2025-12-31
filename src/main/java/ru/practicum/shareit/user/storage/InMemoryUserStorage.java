package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    public Optional<User> getUser(Integer id) {
        if (id <= 0) {
            throw new ValidationException("ID должен быть больше нуля");
        }
        Optional<User> user = Optional.ofNullable(users.get(id));
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    public User addNew(User user) {
        User newUser = User.builder()
                .id(id++)
                .email(user.getEmail())
                .name(user.getName())
                .build();

        users.put(newUser.getId(), newUser);
        log.info("Добавлен новый пользователь: '{}' (ID: {})",
                newUser.getName(),
                newUser.getId());
        return user;
    }

    public void delete(Integer id) {
        users.remove(id);
    }

    public boolean existsById(Integer id) {
        return users.containsKey(id);
    }

    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }
}
