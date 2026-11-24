package com.mkii.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorResponseDTO {
    private String id;
    private Long sensorNodeId;
    private String sensorCode;
    private String type;
    private String unit;
    private Boolean enabled;
}




