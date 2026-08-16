package com.placement.pms.service;

import com.placement.pms.dto.PlacementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlacementService {
    PlacementDTO createPlacement(PlacementDTO dto);
    PlacementDTO getPlacementById(Long id);
    Page<PlacementDTO> getAllPlacements(Pageable pageable);
}
