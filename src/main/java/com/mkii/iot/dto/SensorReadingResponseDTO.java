package com.mkii.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorReadingResponseDTO {
    private Long readingId;
    private String sensorId;
    private Instant timestamp;
    private String plotId;
    private BigDecimal ph;
    private BigDecimal ec;
    private BigDecimal soilMoisture;
    private BigDecimal nitrogen;
    private BigDecimal phosphorus;
    private BigDecimal potassium;
    private String qualityStatus;
}
