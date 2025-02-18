package com.remopark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityDTO {
    private String type;
    private String icon;
    private String description;
    private String timestamp;
} 