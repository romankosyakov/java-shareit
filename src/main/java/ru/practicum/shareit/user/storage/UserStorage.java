package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getUser(Integer id);

    List<User> getAllUsers();

    User addNew(User user);

    void delete(Integer id);

    boolean existsById(Integer id);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    void update(User user);
}
