package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.validation.UserExists;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Item {

    @NotNull(groups = UpdateValidation.class, message = "ID вещи обязателен для обновления")
    private Integer id;

    private String name;

    private String description;

        private boolean available;

    @NotBlank(message = "У вещи должен быть владелец", groups = CreateValidation.class)
    @UserExists
    private Integer userId;

    //private ItemRequest request; заготовка для дальнейшей разработки
}