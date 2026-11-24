package com.mkii.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorNodeResponseDTO {
    private Long id;
    private Long gatewayId;
    private String nodeCode;
    private String name;
    private String status;
}



