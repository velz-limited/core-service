package com.velz.service.core.location.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class RegionSearchResponse {

    private UUID id;
    private String regionName;
    private String countryName;
}
