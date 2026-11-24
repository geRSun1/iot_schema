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
public class GatewayResponseDTO {
    private Long id;
    private String gatewayCode;
    private String name;
    private String status;
    private LocalDateTime lastSeenAt;
}



