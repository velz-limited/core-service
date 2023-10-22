package com.velz.service.core.location.region;

import com.velz.service.core._base.AppProperties;
import com.velz.service.core.location.country.Country;
import com.velz.service.core.location.response.FeaturesGeoJsonResponse;
import com.velz.service.core.location.response.RegionSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private AppProperties appProperties;

    public Page<Region> getAllByCountry(Country country, Pageable pageable) {
        return regionRepository.findAllByCountry(country, pageable);
    }

    public Region getById(UUID id) {
        return regionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Region not found."));
    }

    public Page<Region> search(String query, List<UUID> countryIds, Pageable pageable) {
        if (isEmpty(countryIds)) {
            return regionRepository.search(query, pageable);
        }
        return regionRepository.searchByCountry(query, countryIds, pageable);
    }

    public List<RegionSearchResponse> quickSearch(String query, List<UUID> countryIds) {
        Pageable pageable = PageRequest.of(0, appProperties.getSearch().getQuick().getMaxResults());
        if (isEmpty(countryIds)) {
            return regionRepository.quickSearch(query, pageable);
        }
        return regionRepository.quickSearchByCountry(query, countryIds, pageable);
    }

    public FeaturesGeoJsonResponse toGeoJson(List<Region> regions) {
        FeaturesGeoJsonResponse response = new FeaturesGeoJsonResponse();
        response.setName("Region Feature Collection");
        try {
            for (Region region : regions) {
                Map<String, String> properties = new HashMap<>();
                properties.put("region_id", region.getId().toString());
                properties.put("region_name", region.getName());

                FeaturesGeoJsonResponse.Feature feature = new FeaturesGeoJsonResponse.Feature();
                feature.setProperties(properties);
                feature.setGeometry(region.getBoundary());
                response.getFeatures().add(feature);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public FeaturesGeoJsonResponse toGeoJson(Region region) {
        return toGeoJson(List.of(region));
    }

    public FeaturesGeoJsonResponse getAllGeoJsonByCountry(Country country) {
        return toGeoJson(regionRepository.findAllByCountry(country));
    }

    public FeaturesGeoJsonResponse getGeoJson(Region region) {
        return toGeoJson(region);
    }
}
