package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {

    ItemResponseDto addNewItem(Integer userId, NewItemDto newItemDto);

    ItemResponseDto updateItem(Integer userId, Integer itemId, UpdateItemDto updateItemDto);

    ItemResponseDto getItem(Integer itemId);

    List<ItemResponseDto> searchItems(String text);

    List<ItemResponseDto> getUserItems(Integer userId);
}