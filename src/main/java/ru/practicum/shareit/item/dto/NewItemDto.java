package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewItemDto {

    private String name;

    private String description;

    @Positive
    private Integer userId;

    private Boolean available;
}
