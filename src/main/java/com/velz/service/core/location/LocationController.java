package com.velz.service.core.location;

import com.velz.service.core.location.country.Country;
import com.velz.service.core.location.country.CountryService;
import com.velz.service.core.location.region.Region;
import com.velz.service.core.location.region.RegionService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationController {

    @Autowired
    private CountryService countryService;

    @Autowired
    private RegionService regionService;

    @GetMapping("/country")
    public Page<Country> getAllCountries(
            @ParameterObject
            @PageableDefault(size = 50)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            @SortDefault(sort = "alpha3Code", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return countryService.getAll(pageable);
    }

    @GetMapping("/country/{countryId}/region")
    public Page<Region> getAllRegionsByCountry(
            @ParameterObject
            @PageableDefault(size = 50)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,
            @PathVariable UUID countryId) {
        return regionService.getAllByCountry(countryService.getById(countryId), pageable);
    }

    @GetMapping("/country/{countryId}")
    public Country getCountry(@PathVariable UUID countryId) {
        return countryService.getById(countryId);
    }

    @GetMapping("/region/{regionId}")
    public Region getRegion(@PathVariable UUID regionId) {
        return regionService.getById(regionId);
    }
}
