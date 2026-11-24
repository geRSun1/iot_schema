package com.mkii.iot.rest;

import com.mkii.iot.dto.PlotRequestDTO;
import com.mkii.iot.dto.PlotResponseDTO;
import com.mkii.iot.entity.Plot;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = {"PlotRest"})
@SwaggerDefinition(tags = {@Tag(name = "PlotRest", description = "REST APIs for Plot module")})
public interface PlotRest {

    @PostMapping(value = {"", "/create"})
    @ApiOperation(value = "create new Plot", tags = {"create"})
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"), @ApiResponse(code = 400, message = "Bad Request")})
    ResponseEntity<Plot> createPlot(@Valid @RequestBody PlotRequestDTO requestDTO);

    @PutMapping("/{id}")
    ResponseEntity<Plot> updatePlot(@PathVariable String id, @Valid @RequestBody PlotRequestDTO requestDTO);

    @PostMapping("/update")
    ResponseEntity<Map<String, String>> updatePlotByMap(@RequestBody Map<String, Object> requestMap);

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, String>> deletePlot(@PathVariable String id);

    @PostMapping("/delete")
    ResponseEntity<Map<String, String>> deletePlotByMap(@RequestBody Map<String, String> requestMap);

    @GetMapping("/{id}")
    ResponseEntity<PlotResponseDTO> getPlotById(@PathVariable String id);

    @GetMapping(value = {"", "/list"})
    ResponseEntity<List<PlotResponseDTO>> getAllPlots();

    @PostMapping("/batch")
    ResponseEntity<List<PlotResponseDTO>> createPlotsBatch(@RequestBody List<PlotRequestDTO> requestDTOs);

    @PostMapping("/batch/map")
    ResponseEntity<Map<String, Object>> createPlotsBatchMap(@RequestBody List<PlotRequestDTO> requestDTOs);

    @GetMapping("/by-field")
    ResponseEntity<PlotResponseDTO> getPlotByField(@RequestParam String field, @RequestParam String value);

    @GetMapping("/by-field-list")
    ResponseEntity<List<PlotResponseDTO>> getPlotsByFieldList(@RequestParam String field, @RequestParam String value);

    @GetMapping("/by-field-paged")
    ResponseEntity<Page<PlotResponseDTO>> getPlotsByFieldPaged(@RequestParam String field, @RequestParam String value, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/spec-search")
    ResponseEntity<List<PlotResponseDTO>> searchPlotsBySpecification(@RequestParam String search);

    @GetMapping("/spec-search-sorted")
    ResponseEntity<List<PlotResponseDTO>> searchPlotsBySpecificationSorted(@RequestParam String search, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/spec-search-paged")
    ResponseEntity<Page<PlotResponseDTO>> searchPlotsBySpecificationPaged(@RequestParam String search, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/sorted")
    ResponseEntity<List<PlotResponseDTO>> getAllPlotsSorted(@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/paged")
    ResponseEntity<Page<PlotResponseDTO>> getAllPlotsPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction);

    @GetMapping("/count")
    ResponseEntity<Map<String, Object>> getPlotCount();
}
