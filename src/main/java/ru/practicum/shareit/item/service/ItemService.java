package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemResponseDto addNewItem(Long userId, NewItemDto newItemDto);

    ItemResponseDto updateItem(Long userId, Long itemId, UpdateItemDto updateItemDto);

    ItemResponseDto getItem(Long itemId, Long userId);

    List<ItemResponseDto> searchItems(String text);

    List<ItemResponseDto> getUserItems(Long userId);

    public CommentDto addComment(Long userId, Long itemId, NewCommentDto dto);
}