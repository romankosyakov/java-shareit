package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewItemDto {

    @NotBlank(message = "Название вещи не может быть пустым", groups = CreateValidation.class)
    @Size(max = 100, message = "Название вещи не может быть длиннее 100 символов",
            groups = {CreateValidation.class, UpdateValidation.class})
    private String name;

    @NotBlank(message = "Описание вещи не может быть пустым", groups = CreateValidation.class)
    @Size(max = 200, message = "Описание вещи не может быть длиннее 200 символов",
            groups = {CreateValidation.class, UpdateValidation.class})
    private String description;

    private Boolean available;
}
