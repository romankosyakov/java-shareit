package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewItemDto {

    @NotBlank(message = "Название вещи не может быть пустым")
    @Size(max = 100, message = "Название вещи не может быть длиннее 100 символов")
    private String name;

    @NotBlank(message = "Описание вещи не может быть пустым")
    @Size(max = 200, message = "Описание вещи не может быть длиннее 200 символов")
    private String description;

    private Boolean available;
}
