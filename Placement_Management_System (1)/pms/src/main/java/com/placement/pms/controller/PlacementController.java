package com.placement.pms.controller;

import com.placement.pms.dto.PlacementDTO;
import com.placement.pms.service.PlacementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/placements")
@RequiredArgsConstructor
public class PlacementController {

    private final PlacementService placementService;

    @PostMapping
    public ResponseEntity<PlacementDTO> createPlacement(@Valid @RequestBody PlacementDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placementService.createPlacement(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlacementDTO> getPlacement(@PathVariable Long id) {
        return ResponseEntity.ok(placementService.getPlacementById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PlacementDTO>> getAllPlacements(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(placementService.getAllPlacements(pageable));
    }
}
