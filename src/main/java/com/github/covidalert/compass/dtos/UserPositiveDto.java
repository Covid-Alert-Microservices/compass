package com.github.covidalert.compass.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UserPositiveDto {
    @NotNull
    private final String userId;

    @Min(0)
    private final Long timestamp;

    public UserPositiveDto(String userId, Long timestamp) {
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
