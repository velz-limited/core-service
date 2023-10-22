package com.velz.service.core.location.region;

import com.velz.service.core.location.country.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegionRepository extends JpaRepository<Region, UUID> {

    Page<Region> findAllByCountry(Country country, Pageable pageable);
}
