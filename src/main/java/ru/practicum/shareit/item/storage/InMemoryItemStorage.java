package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    private static final Map<Integer, Item> items = new HashMap<>();
    private Integer id = 1;

    @Override
    public Optional<Item> getItem(Integer itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> searchItems(String query) {
        return items.values()
                .stream()
                .filter(item -> item.isAvailable() && (item.getName().toLowerCase().contains(query)
                        || item.getDescription().toLowerCase().contains(query)))
                .toList();
    }

    @Override
    public Item addNewItem(Item item) {
        Item newItem = Item.builder()
                .id(id++)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .userId(item.getUserId())
                .build();
        items.put(newItem.getId(), newItem);
        log.info("Добавлена новая вещь: '{}' (ID: {})", newItem.getName(), newItem.getId());
        return newItem;
    }

    @Override
    public List<Item> getUserItems(Integer userId) {
        return items.values()
                .stream()
                .filter(item -> Objects.equals(item.getUserId(), userId))
                .toList();
    }

    @Override
    public void update(Item item) {
        if (items.containsKey(item.getId())) {
            items.put(item.getId(), item);
            log.info("Обновлена вещь в хранилище: '{}' (ID: {})", item.getName(), item.getId());
        }
    }
}
