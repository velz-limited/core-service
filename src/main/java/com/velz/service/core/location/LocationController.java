package com.velz.service.core.location;

import com.velz.service.core.location.country.Country;
import com.velz.service.core.location.country.CountryService;
import com.velz.service.core.location.region.Region;
import com.velz.service.core.location.region.RegionService;
import com.velz.service.core.location.response.CountrySearchResponse;
import com.velz.service.core.location.response.FeaturesGeoJsonResponse;
import com.velz.service.core.location.response.RegionSearchResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/search/paged/country/{query}")
    public Page<Country> searchCountry(
            @ParameterObject
            @PageableDefault(size = 50)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            @SortDefault(sort = "alpha3Code", direction = Sort.Direction.ASC)
            Pageable pageable,
            @PathVariable String query) {

        return countryService.search(query, pageable);
    }

    @GetMapping("/search/paged/region/{query}")
    public Page<Region> searchRegion(
            @ParameterObject
            @PageableDefault(size = 50)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,
            @PathVariable String query,
            @RequestParam(required = false) List<UUID> countryIds) {

        return regionService.search(query, countryIds, pageable);
    }

    @GetMapping("/search/quick/country/{query}")
    public List<CountrySearchResponse> quickSearchCountry(
            @PathVariable String query) {

        return countryService.quickSearch(query);
    }

    @GetMapping("/search/quick/region/{query}")
    public List<RegionSearchResponse> quickSearchRegion(
            @PathVariable String query,
            @RequestParam(required = false) List<UUID> countryIds) {

        return regionService.quickSearch(query, countryIds);
    }

    @GetMapping("/geojson/country")
    public FeaturesGeoJsonResponse geoJsonAllCountries() {
        return countryService.getAllGeoJson();
    }

    @GetMapping("/geojson/country/{countryId}")
    public FeaturesGeoJsonResponse geoJsonByCountry(@PathVariable UUID countryId) {
        return countryService.getGeoJson(countryService.getById(countryId));
    }

    @GetMapping("/geojson/country/{countryId}/region")
    public FeaturesGeoJsonResponse geoJsonAllRegionByCountry(@PathVariable UUID countryId) {
        return regionService.getAllGeoJsonByCountry(countryService.getById(countryId));
    }

    @GetMapping("/geojson/region/{regionId}")
    public FeaturesGeoJsonResponse geoJsonByRegion(@PathVariable UUID regionId) {
        return regionService.getGeoJson(regionService.getById(regionId));
    }
}
