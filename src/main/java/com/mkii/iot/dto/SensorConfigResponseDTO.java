package com.mkii.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorConfigResponseDTO {
    private Long id;
    private String sensorId;
    private Integer readIntervalSeconds;
    private Double minThreshold;
    private Double maxThreshold;
    private Boolean enabled;
    private LocalDateTime updatedAt;
}




