package com.velz.service.core.location.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    public Page<Country> getAll(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }

    public Country getById(UUID id) {
        return countryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Country not found."));
    }
}
