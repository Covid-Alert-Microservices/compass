package com.github.covidalert.microservicetemplate.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UserPositiveDto {
    @NotNull
    private String userId;

    @Min(0)
    private Long timestamp;

    public String getUserId() {
        return userId;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
