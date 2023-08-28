package com.velz.service.core.attraction.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttractionTypeRepository extends JpaRepository<AttractionType, UUID> {
}
