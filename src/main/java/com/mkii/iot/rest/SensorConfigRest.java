package com.mkii.iot.rest;

import com.mkii.iot.dto.SensorConfigRequestDTO;
import com.mkii.iot.dto.SensorConfigResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@Api(tags = {"SensorConfigRest"})
@SwaggerDefinition(tags = {@Tag(name = "SensorConfigRest", description = "REST APIs for Sensor configuration module")})
public interface SensorConfigRest {

    @PutMapping("/{sensorId}/config")
    @ApiOperation(value = "save Sensor configuration", tags = {"update"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Sensor not found")
    })
    ResponseEntity<SensorConfigResponseDTO> saveConfig(
            @ApiParam(value = "Sensor UUID", required = true) @PathVariable String sensorId,
            @Valid @RequestBody SensorConfigRequestDTO requestDTO);

    @GetMapping("/{sensorId}/config")
    @ApiOperation(value = "get Sensor configuration", tags = {"getBySensor"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "Sensor configuration not found")
    })
    ResponseEntity<SensorConfigResponseDTO> getConfig(
            @ApiParam(value = "Sensor UUID", required = true) @PathVariable String sensorId);

    @DeleteMapping("/{sensorId}/config")
    ResponseEntity<Map<String, String>> deleteConfig(@PathVariable String sensorId);

    @GetMapping("/config/count")
    ResponseEntity<Map<String, Object>> getConfigCount();
}
