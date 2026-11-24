package com.mkii.iot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorConfigRequestDTO {
    @NotNull(message = "Chu kỳ đọc không được để trống")
    @Positive(message = "Chu kỳ đọc phải lớn hơn 0")
    private Integer readIntervalSeconds;

    private Double minThreshold;
    private Double maxThreshold;
    private Boolean enabled;
}



