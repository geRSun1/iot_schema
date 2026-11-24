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
public class SensorReadingRequestDTO {

    @NotBlank(message = "Mã cảm biến không được để trống")
    @Size(max = 36, message = "Mã cảm biến không hợp lệ")
    private String sensorId;

    @NotBlank(message = "Mã ô đất không được để trống")
    @Size(max = 36, message = "Mã ô đất không hợp lệ")
    private String plotId;

    @NotNull(message = "Thời điểm đo không được để trống")
    private java.time.Instant timestamp;

    private java.math.BigDecimal ph;
    private java.math.BigDecimal ec;
    private java.math.BigDecimal soilMoisture;
    private java.math.BigDecimal nitrogen;
    private java.math.BigDecimal phosphorus;
    private java.math.BigDecimal potassium;

    @NotBlank(message = "Trạng thái chất lượng không được để trống")
    @Size(max = 50, message = "Trạng thái chất lượng không được vượt quá 50 ký tự")
    private String qualityStatus;
}
