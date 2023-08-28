package com.velz.service.core.location.area;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AreaRepository extends JpaRepository<Area, UUID> {
}
