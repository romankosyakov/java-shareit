package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemResponseDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Integer id) {
        log.debug("Вызван метод получения списка вещей пользователя с ID: {}", id);
        return itemService.getUserItems(id);
    }

    @GetMapping("/{id}")
    public ItemResponseDto getItem(@PathVariable("id") Integer itemId) {
        log.debug("Вызван метод получения вещи с ID: {}", itemId);
        return itemService.getItem(itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto addNewItem(@Valid @RequestHeader("X-Sharer-User-Id") Integer id,
                                      @RequestBody @Valid NewItemDto newItemDto) {
        return itemService.addNewItem(id, newItemDto);
    }

    @PatchMapping("/{id}")
    public ItemResponseDto updateItem(@Valid
                                      @RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @RequestBody UpdateItemDto updateItemDto,
                                      @PathVariable("id") Integer id) {
        return itemService.updateItem(userId, id, updateItemDto);
    }

    @GetMapping("/search")
    public Collection<ItemResponseDto> searchItems(@RequestParam(value = "text", required = false) String text) {
        return itemService.searchItems(text);
    }
}
