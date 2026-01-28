package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {

    @Size(max = 100, message = "Имя пользователя не может быть длиннее 100 символов")
    private String name;

    @Email(message = "Некорректный формат email. Пример: user@example.com")
    private String email;
}