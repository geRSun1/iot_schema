package com.mkii.iot.rest;

import com.mkii.iot.dto.SensorNodeRequestDTO;
import com.mkii.iot.dto.SensorNodeResponseDTO;
import com.mkii.iot.entity.SensorNode;
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

@Api(tags = {"SensorNodeRest"})
@SwaggerDefinition(tags = {@Tag(name = "SensorNodeRest", description = "REST APIs for Sensor Node module")})
public interface SensorNodeRest {

    @PostMapping(value = {"", "/create"})
    @ApiOperation(value = "create new Sensor Node", tags = {"create"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Gateway not found")
    })
    ResponseEntity<SensorNode> createNode(@Valid @RequestBody SensorNodeRequestDTO requestDTO);

    @PutMapping("/{id}")
    ResponseEntity<SensorNode> updateNode(@PathVariable Long id, @Valid @RequestBody SensorNodeRequestDTO requestDTO);

    @PostMapping("/update")
    ResponseEntity<Map<String, String>> updateNodeByMap(@RequestBody Map<String, Object> requestMap);

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, String>> deleteNode(@PathVariable Long id);

    @PostMapping("/delete")
    ResponseEntity<Map<String, String>> deleteNodeByMap(@RequestBody Map<String, String> requestMap);

    @GetMapping("/{id}")
    ResponseEntity<SensorNodeResponseDTO> getNodeById(@PathVariable Long id);

    @GetMapping(value = {"", "/list"})
    ResponseEntity<List<SensorNodeResponseDTO>> getAllNodes();

    @GetMapping("/gateway/{gatewayId}")
    @ApiOperation(value = "get Sensor Nodes by Gateway", tags = {"list"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = List.class),
            @ApiResponse(code = 404, message = "Gateway not found")
    })
    ResponseEntity<List<SensorNodeResponseDTO>> getNodesByGateway(
            @ApiParam(value = "Gateway ID", required = true) @PathVariable Long gatewayId);

    @GetMapping("/by-field")
    ResponseEntity<SensorNodeResponseDTO> getNodeByField(@RequestParam String field, @RequestParam String value);

    @GetMapping("/by-field-list")
    ResponseEntity<List<SensorNodeResponseDTO>> getNodesByFieldList(@RequestParam String field, @RequestParam String value);

    @GetMapping("/by-field-paged")
    ResponseEntity<Page<SensorNodeResponseDTO>> getNodesByFieldPaged(@RequestParam String field, @RequestParam String value, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/spec-search")
    ResponseEntity<List<SensorNodeResponseDTO>> searchNodesBySpecification(@RequestParam String search);

    @GetMapping("/spec-search-sorted")
    ResponseEntity<List<SensorNodeResponseDTO>> searchNodesBySpecificationSorted(@RequestParam String search, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/spec-search-paged")
    ResponseEntity<Page<SensorNodeResponseDTO>> searchNodesBySpecificationPaged(@RequestParam String search, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @PostMapping("/batch")
    ResponseEntity<List<SensorNodeResponseDTO>> createNodesBatch(@RequestBody List<SensorNodeRequestDTO> requestDTOs);

    @PostMapping("/batch/map")
    ResponseEntity<Map<String, Object>> createNodesBatchMap(@RequestBody List<SensorNodeRequestDTO> requestDTOs);

    @GetMapping("/sorted")
    ResponseEntity<List<SensorNodeResponseDTO>> getAllNodesSorted(@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/paged")
    ResponseEntity<Page<SensorNodeResponseDTO>> getAllNodesPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/count")
    ResponseEntity<Map<String, Object>> getNodeCount();
}
