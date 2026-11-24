package com.mkii.iot.rest;

import com.mkii.iot.dto.SensorRequestDTO;
import com.mkii.iot.dto.SensorResponseDTO;
import com.mkii.iot.entity.Sensor;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

@Api(tags = {"SensorRest"})
@SwaggerDefinition(tags = {@Tag(name = "SensorRest", description = "REST APIs for Sensor module")})
public interface SensorRest {

    @PostMapping(value = {"", "/create"})
    @ApiOperation(value = "create new Sensor", tags = {"create"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Sensor Node not found")
    })
    ResponseEntity<Sensor> createSensor(@Valid @RequestBody SensorRequestDTO requestDTO);

    @PutMapping("/{id}")
    ResponseEntity<Sensor> updateSensor(@PathVariable String id, @Valid @RequestBody SensorRequestDTO requestDTO);

    @PostMapping("/update")
    ResponseEntity<Map<String, String>> updateSensorByMap(@RequestBody Map<String, Object> requestMap);

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, String>> deleteSensor(@PathVariable String id);

    @PostMapping("/delete")
    ResponseEntity<Map<String, String>> deleteSensorByMap(@RequestBody Map<String, String> requestMap);

    @GetMapping("/{id}")
    ResponseEntity<SensorResponseDTO> getSensorById(@PathVariable String id);

    @GetMapping(value = {"", "/list"})
    ResponseEntity<List<SensorResponseDTO>> getAllSensors();

    @GetMapping("/node/{nodeId}")
    @ApiOperation(value = "get Sensors by Sensor Node", tags = {"list"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = List.class),
            @ApiResponse(code = 404, message = "Sensor Node not found")
    })
    ResponseEntity<List<SensorResponseDTO>> getSensorsByNode(
            @ApiParam(value = "Sensor Node ID", required = true) @PathVariable Long nodeId);

    @GetMapping("/search")
    @ApiOperation(value = "search Sensors by code or type", tags = {"search"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = List.class)
    })
    ResponseEntity<List<SensorResponseDTO>> searchSensors(
            @ApiParam(value = "Keyword", required = true) @RequestParam String keyword);

    @GetMapping("/by-field")
    ResponseEntity<SensorResponseDTO> getSensorByField(@RequestParam String field, @RequestParam String value);

    @GetMapping("/by-field-list")
    ResponseEntity<List<SensorResponseDTO>> getSensorsByFieldList(@RequestParam String field, @RequestParam String value);

    @GetMapping("/by-field-paged")
    ResponseEntity<Page<SensorResponseDTO>> getSensorsByFieldPaged(@RequestParam String field, @RequestParam String value, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/spec-search")
    ResponseEntity<List<SensorResponseDTO>> searchSensorsBySpecification(@RequestParam String search);

    @GetMapping("/spec-search-sorted")
    ResponseEntity<List<SensorResponseDTO>> searchSensorsBySpecificationSorted(@RequestParam String search, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/spec-search-paged")
    ResponseEntity<Page<SensorResponseDTO>> searchSensorsBySpecificationPaged(@RequestParam String search, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @PostMapping("/batch")
    ResponseEntity<List<SensorResponseDTO>> createSensorsBatch(@RequestBody List<SensorRequestDTO> requestDTOs);

    @PostMapping("/batch/map")
    ResponseEntity<Map<String, Object>> createSensorsBatchMap(@RequestBody List<SensorRequestDTO> requestDTOs);

    @GetMapping("/sorted")
    ResponseEntity<List<SensorResponseDTO>> getAllSensorsSorted(@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/paged")
    ResponseEntity<Page<SensorResponseDTO>> getAllSensorsPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/count")
    ResponseEntity<Map<String, Object>> getSensorCount();
}
