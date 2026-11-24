package com.mkii.iot.rest;

import com.mkii.iot.dto.GatewayRequestDTO;
import com.mkii.iot.dto.GatewayResponseDTO;
import com.mkii.iot.entity.Gateway;
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

@Api(tags = {"GatewayRest"})
@SwaggerDefinition(tags = {@Tag(name = "GatewayRest", description = "REST APIs for Gateway module")})
public interface GatewayRest {

    @PostMapping(value = {"", "/create"})
    @ApiOperation(value = "create new Gateway", tags = {"create"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 409, message = "Gateway code already exists")
    })
    ResponseEntity<Gateway> createGateway(@Valid @RequestBody GatewayRequestDTO requestDTO);

    @GetMapping(value = {"", "/list"})
    @ApiOperation(value = "get all Gateways", tags = {"list"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = List.class)
    })
    ResponseEntity<List<GatewayResponseDTO>> getAllGateways();

    @PutMapping("/{id}")
    ResponseEntity<Gateway> updateGateway(@PathVariable Long id, @Valid @RequestBody GatewayRequestDTO requestDTO);

    @PostMapping("/update")
    ResponseEntity<Map<String, String>> updateGatewayByMap(@RequestBody Map<String, Object> requestMap);

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, String>> deleteGateway(@PathVariable Long id);

    @PostMapping("/delete")
    ResponseEntity<Map<String, String>> deleteGatewayByMap(@RequestBody Map<String, String> requestMap);

    @GetMapping("/{id}")
    ResponseEntity<GatewayResponseDTO> getGatewayById(@PathVariable Long id);

    @GetMapping("/search")
    ResponseEntity<List<GatewayResponseDTO>> searchGateways(@RequestParam String keyword);

    @GetMapping("/by-field")
    ResponseEntity<GatewayResponseDTO> getGatewayByField(@RequestParam String field, @RequestParam String value);

    @GetMapping("/by-field-list")
    ResponseEntity<List<GatewayResponseDTO>> getGatewaysByFieldList(@RequestParam String field, @RequestParam String value);

    @GetMapping("/by-field-paged")
    ResponseEntity<Page<GatewayResponseDTO>> getGatewaysByFieldPaged(@RequestParam String field, @RequestParam String value, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/spec-search")
    ResponseEntity<List<GatewayResponseDTO>> searchGatewaysBySpecification(@RequestParam String search);

    @GetMapping("/spec-search-sorted")
    ResponseEntity<List<GatewayResponseDTO>> searchGatewaysBySpecificationSorted(@RequestParam String search, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/spec-search-paged")
    ResponseEntity<Page<GatewayResponseDTO>> searchGatewaysBySpecificationPaged(@RequestParam String search, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @PostMapping("/batch")
    ResponseEntity<List<GatewayResponseDTO>> createGatewaysBatch(@RequestBody List<GatewayRequestDTO> requestDTOs);

    @PostMapping("/batch/map")
    ResponseEntity<Map<String, Object>> createGatewaysBatchMap(@RequestBody List<GatewayRequestDTO> requestDTOs);

    @GetMapping("/sorted")
    ResponseEntity<List<GatewayResponseDTO>> getAllGatewaysSorted(@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/paged")
    ResponseEntity<Page<GatewayResponseDTO>> getAllGatewaysPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/count")
    ResponseEntity<Map<String, Object>> getGatewayCount();

    @PostMapping("/{id}/heartbeat")
    @ApiOperation(value = "update Gateway heartbeat", tags = {"heartbeat"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "Gateway not found")
    })
    ResponseEntity<GatewayResponseDTO> heartbeat(
            @ApiParam(value = "Gateway ID", required = true) @PathVariable Long id);
}
