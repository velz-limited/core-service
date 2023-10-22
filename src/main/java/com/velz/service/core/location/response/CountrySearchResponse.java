package com.velz.service.core.location.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CountrySearchResponse {

    private UUID id;
    private String countryName;
}
