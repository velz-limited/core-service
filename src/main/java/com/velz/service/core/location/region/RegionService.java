package com.velz.service.core.location.region;

import com.velz.service.core.location.country.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public Page<Region> getAllByCountry(Country country, Pageable pageable) {
        return regionRepository.findAllByCountry(country, pageable);
    }

    public Region getById(UUID id) {
        return regionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Region not found."));
    }
}
