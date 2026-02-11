package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    Optional<Item> getItem(Integer itemId);

    List<Item> searchItems(String query);

    Item addNewItem(Item item);

    List<Item> getUserItems(Integer userId);

    void update(Item item);
}
