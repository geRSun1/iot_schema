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
public class GatewayRequestDTO {
    @NotBlank(message = "Mã Gateway không được để trống")
    @Size(max = 100, message = "Mã Gateway không được vượt quá 100 ký tự")
    private String gatewayCode;

    @NotBlank(message = "Tên Gateway không được để trống")
    @Size(max = 150, message = "Tên Gateway không được vượt quá 150 ký tự")
    private String name;

    public String getGatewayCode() {
        return gatewayCode;
    }

    public void setGatewayCode(String gatewayCode) {
        this.gatewayCode = gatewayCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}



