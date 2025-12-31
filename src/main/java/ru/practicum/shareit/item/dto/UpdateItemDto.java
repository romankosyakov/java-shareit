package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemDto {

    private String name;
    private String description;
    private Boolean available;

}