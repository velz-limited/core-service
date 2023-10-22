package com.velz.service.core.location.country;

import com.velz.service.core._base.AppProperties;
import com.velz.service.core.location.response.CountrySearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private AppProperties appProperties;

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
}
