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
public class SensorRequestDTO {
    @NotNull(message = "Sensor Node không được để trống")
    private Long sensorNodeId;

    @NotBlank(message = "Mã cảm biến không được để trống")
    @Size(max = 100, message = "Mã cảm biến không được vượt quá 100 ký tự")
    private String sensorCode;

    @NotBlank(message = "Loại cảm biến không được để trống")
    @Size(max = 100, message = "Loại cảm biến không được vượt quá 100 ký tự")
    private String type;

    @Size(max = 50, message = "Đơn vị đo không được vượt quá 50 ký tự")
    private String unit;
}



