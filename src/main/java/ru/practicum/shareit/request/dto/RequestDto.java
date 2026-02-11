package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

@Builder
@Data
public class RequestDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String description;

    private UserResponseDto requestor;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime created;

}