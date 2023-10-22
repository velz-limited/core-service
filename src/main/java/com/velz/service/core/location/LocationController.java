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
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationController {

    private static final CacheControl SEARCH_CACHE_CONTROL = CacheControl.maxAge(5, TimeUnit.MINUTES);
    private static final CacheControl GEO_JSON_CACHE_CONTROL = CacheControl.maxAge(12, TimeUnit.HOURS);

    @Autowired
    private CountryService countryService;

    @Autowired
    private RegionService regionService;

    /* Get */
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
    /* /Get */

    /* Search */
    @GetMapping("/search/paged/country/{query}")
    public ResponseEntity<Page<Country>> searchCountry(
            @ParameterObject
            @PageableDefault(size = 50)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            @SortDefault(sort = "alpha3Code", direction = Sort.Direction.ASC)
            Pageable pageable,
            @PathVariable String query) {

        return ok().cacheControl(SEARCH_CACHE_CONTROL).body(countryService.search(query, pageable));
    }

    @GetMapping("/search/paged/region/{query}")
    public ResponseEntity<Page<Region>> searchRegion(
            @ParameterObject
            @PageableDefault(size = 50)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,
            @PathVariable String query,
            @RequestParam(required = false) List<UUID> countryIds) {

        return ok().cacheControl(SEARCH_CACHE_CONTROL).body(regionService.search(query, countryIds, pageable));
    }

    @GetMapping("/search/quick/country/{query}")
    public ResponseEntity<List<CountrySearchResponse>> quickSearchCountry(
            @PathVariable String query) {

        return ok().cacheControl(SEARCH_CACHE_CONTROL).body(countryService.quickSearch(query));
    }

    @GetMapping("/search/quick/region/{query}")
    public ResponseEntity<List<RegionSearchResponse>> quickSearchRegion(
            @PathVariable String query,
            @RequestParam(required = false) List<UUID> countryIds) {

        return ok().cacheControl(SEARCH_CACHE_CONTROL).body(regionService.quickSearch(query, countryIds));
    }
    /* /Search */

    /* GeoJson */
    @GetMapping("/geojson/country")
    public ResponseEntity<FeaturesGeoJsonResponse> geoJsonAllCountries() {
        return ok().cacheControl(GEO_JSON_CACHE_CONTROL).body(countryService.getAllGeoJson());
    }

    @GetMapping("/geojson/country/{countryId}")
    public ResponseEntity<FeaturesGeoJsonResponse> geoJsonByCountry(@PathVariable UUID countryId) {
        return ok().cacheControl(GEO_JSON_CACHE_CONTROL).body(countryService.getGeoJson(countryService.getById(countryId)));
    }

    @GetMapping("/geojson/country/{countryId}/region")
    public ResponseEntity<FeaturesGeoJsonResponse> geoJsonAllRegionByCountry(@PathVariable UUID countryId) {
        return ok().cacheControl(GEO_JSON_CACHE_CONTROL).body(regionService.getAllGeoJsonByCountry(countryService.getById(countryId)));
    }

    @GetMapping("/geojson/region/{regionId}")
    public ResponseEntity<FeaturesGeoJsonResponse> geoJsonByRegion(@PathVariable UUID regionId) {
        return ok().cacheControl(GEO_JSON_CACHE_CONTROL).body(regionService.getGeoJson(regionService.getById(regionId)));
    }
    /* /GeoJson */
}
