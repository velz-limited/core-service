package com.velz.service.core.location.country;

import com.velz.service.core.location.response.CountrySearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {

    @Query("SELECT c " +
            "FROM Country c " +
            "WHERE is_similar(c.name, :query) " +
            "OR is_similar(c.alpha2Code, :query) " +
            "OR is_similar(c.alpha3Code, :query) " +
            "ORDER BY similarity(c.name, :query) DESC")
    Page<Country> search(String query, Pageable pageable);

    @Query("SELECT new com.velz.service.core.location.response.CountrySearchResponse(c.id, c.name) " +
            "FROM Country c " +
            "WHERE is_similar(c.name, :query) " +
            "OR is_similar(c.alpha2Code, :query) " +
            "OR is_similar(c.alpha3Code, :query) " +
            "ORDER BY similarity(c.name, :query) DESC")
    List<CountrySearchResponse> quickSearch(String query, Pageable pageable);
}
