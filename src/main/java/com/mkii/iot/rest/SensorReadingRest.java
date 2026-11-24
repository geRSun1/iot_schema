package com.mkii.iot.rest;

import com.mkii.iot.dto.SensorReadingRequestDTO;
import com.mkii.iot.dto.SensorReadingResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Api(tags = {"SensorReadingRest"})
@SwaggerDefinition(tags = {@Tag(name = "SensorReadingRest", description = "REST APIs for Sensor readings")})
public interface SensorReadingRest {

    @PostMapping(value = {"", "/create"})
    @ApiOperation(value = "receive Sensor Reading", tags = {"create"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Sensor or plot not found")
    })
    ResponseEntity<SensorReadingResponseDTO> receiveReading(
            @Valid @RequestBody SensorReadingRequestDTO requestDTO);

    @PostMapping("/batch")
    ResponseEntity<List<SensorReadingResponseDTO>> receiveReadingsBatch(@RequestBody List<SensorReadingRequestDTO> requestDTOs);

    @GetMapping("/id/{id}")
    ResponseEntity<SensorReadingResponseDTO> getReadingById(@PathVariable Long id);

    @GetMapping("/sensor/{sensorId}")
    @ApiOperation(value = "get Sensor readings by time range", tags = {"list"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = List.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Sensor not found")
    })
    ResponseEntity<List<SensorReadingResponseDTO>> getReadings(
            @ApiParam(value = "Sensor UUID", required = true) @PathVariable String sensorId,
            @ApiParam(value = "Start time in ISO-8601 format", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @ApiParam(value = "End time in ISO-8601 format", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to);

    @GetMapping("/plot/{plotId}")
    ResponseEntity<List<SensorReadingResponseDTO>> getReadingsByPlot(
            @PathVariable String plotId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to);

    @GetMapping("/count")
    ResponseEntity<Map<String, Object>> getReadingCount();
}
