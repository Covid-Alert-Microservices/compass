package com.github.covidalert.covidtests.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UserPositiveDTO {
    @NotNull
    private String userId;

    @Min(0)
    private Long timestamp;

    public UserPositiveDTO(String userId, Long timestamp) {
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public UserPositiveDTO() {}

    public String getUserId() {
        return userId;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
