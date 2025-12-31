package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.item.mapper.ItemMapper;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemServiceImpl implements ItemService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    public ItemResponseDto addNewItem(Integer userId, NewItemDto newItemDto) {
        userStorage.getUser(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с itemId: " + userId + " не найден."));

        boolean hasNotAvailableValue = newItemDto.getAvailable() == null;
        boolean hasNotNameValue = newItemDto.getName() == null || newItemDto.getName().isBlank();
        boolean hasNotDescriptionValue = newItemDto.getDescription() == null;

        if (hasNotAvailableValue || hasNotNameValue || hasNotDescriptionValue) {
            throw new ValidationException("Название, описание и статус вещи не могут быть пустым");
        }

        Item item = ItemMapper.toEntity(newItemDto, userId);
        Item savedItem = itemStorage.addNewItem(item);

        return ItemMapper.toResponseDto(savedItem);
    }

    public ItemResponseDto updateItem(Integer userId, Integer itemId, UpdateItemDto updateItemDto) {

        userStorage.getUser(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID: " + userId + " не найден"));

        Item item = itemStorage.getItem(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + itemId + " не найдена"));

        if (!Objects.equals(userId, item.getUserId())) {
            throw new ValidationException("Изменять вещь может только ее владелец");
        }

        ItemMapper.updateEntityFromDto(updateItemDto, item);

        itemStorage.update(item);

        log.info("Обновлена вещь: '{}' (ID: {})", item.getName(), item.getId());
        return ItemMapper.toResponseDto(item);
    }

    public ItemResponseDto getItem(Integer itemId) {
        return itemStorage.getItem(itemId)
                .map(ItemMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Вещь c id: " + itemId + " не найдена"));
    }

    public List<ItemResponseDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemStorage.searchItems(text.toLowerCase())
                .stream()
                .map(ItemMapper::toResponseDto)
                .toList();
    }

    public List<ItemResponseDto> getUserItems(Integer userId) {
        return itemStorage.getUserItems(userId)
                .stream()
                .map(ItemMapper::toResponseDto)
                .toList();
    }
}