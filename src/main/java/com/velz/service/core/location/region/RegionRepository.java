package com.velz.service.core.location.region;

import com.velz.service.core.location.country.Country;
import com.velz.service.core.location.response.RegionSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegionRepository extends JpaRepository<Region, UUID> {

    Page<Region> findAllByCountry(Country country, Pageable pageable);

    @Query("SELECT r " +
            "FROM Region r " +
            "WHERE r.country = :country")
    List<Region> findAllByCountry(Country country);

    @Query("SELECT r " +
            "FROM Region r " +
            "WHERE is_similar(r.name, :query) " +
            "ORDER BY similarity(r.name, :query) DESC")
    Page<Region> search(String query, Pageable pageable);

    @Query("SELECT r " +
            "FROM Region r " +
            "WHERE r.country.id IN :countryIds " +
            "AND is_similar(r.name, :query) " +
            "ORDER BY similarity(r.name, :query) DESC")
    Page<Region> searchByCountry(String query, List<UUID> countryIds, Pageable pageable);

    @Query("SELECT new com.velz.service.core.location.response.RegionSearchResponse(r.id, r.name, c.name) " +
            "FROM Region r " +
            "JOIN Country c ON c.id = r.country.id " +
            "WHERE is_similar(r.name, :query) " +
            "ORDER BY similarity(r.name, :query) DESC")
    List<RegionSearchResponse> quickSearch(String query, Pageable pageable);

    @Query("SELECT new com.velz.service.core.location.response.RegionSearchResponse(r.id, r.name, c.name) " +
            "FROM Region r " +
            "JOIN Country c ON c.id = r.country.id " +
            "WHERE r.country.id IN :countryIds " +
            "AND is_similar(r.name, :query) " +
            "ORDER BY similarity(r.name, :query) DESC")
    List<RegionSearchResponse> quickSearchByCountry(String query, List<UUID> countryIds, Pageable pageable);
}
