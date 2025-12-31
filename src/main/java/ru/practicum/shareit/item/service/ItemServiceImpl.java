package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.item.mapper.ItemMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    public ItemResponseDto addNewItem(Integer userId, NewItemDto newItemDto) {
        userStorage.getUser(userId)
                .orElseThrow(() -> new ValidationException("Пользователь с itemId: " + userId + "не найден."));
        return ItemMapper.toResponseDto(itemStorage.addNewItem(ItemMapper.toEntity(newItemDto, userId)));
    }

    public ItemResponseDto updateItem(Integer userId, Integer itemId, UpdateItemDto updateItemDto) {

        User user = userStorage.getUser(userId)
                .orElseThrow(() -> new ValidationException("Пользователь с ID: " + userId + "не найден."));

        Item item;
        Optional<Item> optionalItem = itemStorage.getItem(itemId);

        if (optionalItem.isPresent()) {
            item = optionalItem.get();
        } else {
            throw new NotFoundException("Вещь c id: " + itemId + " не найдена");
        }

        if (!Objects.equals(user.getId(), item.getUserId())) {
            throw new ValidationException("Изменять вещь может только ее владелец");
        }

        ItemMapper.updateEntityFromDto(updateItemDto, item);

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