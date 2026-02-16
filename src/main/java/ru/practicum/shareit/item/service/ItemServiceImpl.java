package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemResponseDto addNewItem(Long userId, NewItemDto newItemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден."));

        Item item = ItemMapper.toEntity(newItemDto, owner);
        Item savedItem = itemRepository.save(item);

        return ItemMapper.toResponseDto(savedItem);
    }

    @Override
    public ItemResponseDto updateItem(Long userId, Long itemId, UpdateItemDto updateItemDto) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена"));

        if (Objects.equals(userId, item.getOwner().getId())) {
            ItemMapper.updateEntityFromDto(updateItemDto, item);

            itemRepository.save(item);

            log.info("Обновлена вещь: '{}' (ID: {})", item.getName(), item.getId());
            return ItemMapper.toResponseDto(item);
        } else {
            throw new ValidationException("Изменять вещь может только ее владелец");
        }

    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto getItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toDto)
                .toList();

        Booking last = null;
        Booking next = null;

        if (item.getOwner().getId().equals(userId)) {
            List<Booking> approvedBookings = bookingRepository
                    .findByItemIdAndStatusOrderByStartAsc(itemId, Status.APPROVED);

            LocalDateTime now = LocalDateTime.now();

            for (Booking booking : approvedBookings) {
                if (booking.getEnd().isBefore(now)) {
                    last = booking;
                } else if (booking.getStart().isAfter(now)) {
                    next = booking;
                    break;
                }
            }
        }

        return ItemMapper.toResponseDto(item, last, next, comments);
    }

    @Override
    public List<ItemResponseDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemRepository.search(text.toLowerCase())
                .stream()
                .map(ItemMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getUserItems(Long userId) {
        List<Item> items = itemRepository.findByOwnerId(userId);
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();

        LocalDateTime now = LocalDateTime.now();

        List<Booking> allBookings = bookingRepository.findAllBookingsForItems(itemIds, Status.APPROVED);
        Map<Long, List<Booking>> bookingsByItem = allBookings.stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));


        Map<Long, List<CommentDto>> commentsMap = commentRepository
                .findByItemIdIn(itemIds)
                .stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),
                        Collectors.mapping(CommentMapper::toDto, Collectors.toList())
                ));

        return items.stream()
                .map(item -> {
                    List<Booking> itemBookings = bookingsByItem.getOrDefault(
                            item.getId(), Collections.emptyList());

                    Booking last = null;
                    Booking next = null;

                    for (Booking booking : itemBookings) {
                        if (booking.getEnd().isBefore(now)) {
                            if (last == null || booking.getEnd().isAfter(last.getEnd())) {
                                last = booking;
                            }
                        } else if (booking.getStart().isAfter(now)) {
                            if (next == null || booking.getStart().isBefore(next.getStart())) {
                                next = booking;
                            }
                        }
                    }

                    List<CommentDto> comments = commentsMap.getOrDefault(
                            item.getId(), Collections.emptyList());

                    return ItemMapper.toResponseDto(item, last, next, comments);
                })
                .toList();
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, NewCommentDto dto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        boolean hasBooking = bookingRepository
                .existsByBookerIdAndItemIdAndStatusAndEndBefore(
                        userId, itemId, Status.APPROVED, LocalDateTime.now()
                );

        if (!hasBooking) {
            throw new ConditionsNotMetException("Пользователь не брал эту вещь в аренду");
        }

        Comment comment = CommentMapper.toComment(dto, item, author);
        comment = commentRepository.save(comment);

        return CommentMapper.toDto(comment);
    }
}