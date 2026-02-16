package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;


public class ItemMapper {

    public static Item toEntity(NewItemDto newItemDto, User owner) {
        return Item.builder()
                .name(newItemDto.getName())
                .description(newItemDto.getDescription())
                .available(newItemDto.getAvailable())
                .owner(owner)
                .build();
    }

    public static ItemResponseDto toResponseDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static void updateEntityFromDto(UpdateItemDto updateItemDto, Item item) {
        if (updateItemDto.getName() != null) {
            item.setName(updateItemDto.getName());
        }
        if (updateItemDto.getDescription() != null) {
            item.setDescription(updateItemDto.getDescription());
        }
        if (updateItemDto.getAvailable() != null) {
            item.setAvailable(updateItemDto.getAvailable());
        }
    }

    public static ItemResponseDto toResponseDto(Item item, Booking lastBooking, Booking nextBooking) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(BookingMapper.toShortDto(lastBooking))
                .nextBooking(BookingMapper.toShortDto(nextBooking))
                .build();
    }

    public static ItemResponseDto toResponseDto(
            Item item,
            Booking lastBooking,
            Booking nextBooking,
            List<CommentDto> comments
    ) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(BookingMapper.toShortDto(lastBooking))
                .nextBooking(BookingMapper.toShortDto(nextBooking))
                .comments(comments)
                .build();
    }
}