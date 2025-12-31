package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @NotNull(groups = UpdateValidation.class, message = "ID пользователя обязателен для обновления")
    private Integer id;

    @NotBlank(message = "Имя пользователя не может быть пустым", groups = CreateValidation.class)
    @Size(max = 100, message = "Имя пользователя не может быть длиннее 100 символов",
            groups = {CreateValidation.class, UpdateValidation.class})
    private String name;

    @NotBlank(message = "Email пользователя не может быть пустым", groups = CreateValidation.class)
    @Email(message = "Некорректный формат email. Пример: user@example.com", groups = {CreateValidation.class, UpdateValidation.class})
    private String email;
}