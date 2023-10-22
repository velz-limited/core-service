package com.velz.service.core.location.country;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velz.service.core._base.AppProperties;
import com.velz.service.core.location.response.CountrySearchResponse;
import com.velz.service.core.location.response.FeaturesGeoJsonResponse;
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

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private ObjectMapper objectMapper;

    public Page<Country> getAll(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }

    public Country getById(UUID id) {
        return countryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Country not found."));
    }

    public Page<Country> search(String query, Pageable pageable) {
        return countryRepository.search(query, pageable);
    }

    public List<CountrySearchResponse> quickSearch(String query) {
        return countryRepository.quickSearch(query, PageRequest.of(0, appProperties.getSearch().getQuick().getMaxResults()));
    }

    public FeaturesGeoJsonResponse toGeoJson(List<Country> countries) {
        FeaturesGeoJsonResponse response = new FeaturesGeoJsonResponse();
        response.setName("Country Feature Collection");
        try {
            for (Country country : countries) {
                Map<String, String> properties = new HashMap<>();
                properties.put("country_id", country.getId().toString());
                properties.put("country_name", country.getName());

                FeaturesGeoJsonResponse.Feature feature = new FeaturesGeoJsonResponse.Feature();
                feature.setProperties(properties);
                feature.setGeometry(country.getBoundary());
                response.getFeatures().add(feature);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public FeaturesGeoJsonResponse toGeoJson(Country country) {
        return toGeoJson(List.of(country));
    }

    public FeaturesGeoJsonResponse getAllGeoJson() {
        return toGeoJson(countryRepository.findAll());
    }

    public FeaturesGeoJsonResponse getGeoJson(Country country) {
        return toGeoJson(country);
    }
}
