package com.example.jdnctamitams.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelResponseDto {
    private Long id;
    private boolean cancelled;

    public CancelResponseDto(Long id, boolean cancelled) {
        this.id = id;
        this.cancelled = cancelled;
    }

}
