package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Item {

    private Integer id;

    private String name;

    private String description;

    private boolean available;

    private Integer userId;

    //private ItemRequest request; заготовка для дальнейшей разработки
}