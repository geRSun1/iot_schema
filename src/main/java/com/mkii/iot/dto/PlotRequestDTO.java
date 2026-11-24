package com.mkii.iot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlotRequestDTO {
    @NotBlank(message = "Tên ô đất không được để trống")
    @Size(max = 150, message = "Tên ô đất không được vượt quá 150 ký tự")
    private String name;
}
