package ru.practicum.shareit.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.storage.UserStorage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserExistsValidator implements ConstraintValidator<UserExists, Integer> {

    private final UserStorage userStorage;

    @Override
    public boolean isValid(Integer userId, ConstraintValidatorContext context) {
        if (userId == null) {
            return false;
        }
        return userStorage.existsById(userId);
    }
}