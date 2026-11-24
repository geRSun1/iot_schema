package com.mkii.iot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorNodeRequestDTO {
    @NotNull(message = "Gateway không được để trống")
    private Long gatewayId;

    @NotBlank(message = "Mã Sensor Node không được để trống")
    @Size(max = 100, message = "Mã Sensor Node không được vượt quá 100 ký tự")
    private String nodeCode;

    @NotBlank(message = "Tên Sensor Node không được để trống")
    @Size(max = 150, message = "Tên Sensor Node không được vượt quá 150 ký tự")
    private String name;
}



