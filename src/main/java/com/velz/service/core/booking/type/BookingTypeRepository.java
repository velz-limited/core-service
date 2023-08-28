package com.velz.service.core.booking.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingTypeRepository extends JpaRepository<BookingType, UUID> {
}
