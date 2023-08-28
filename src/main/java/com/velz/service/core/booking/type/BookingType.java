package com.velz.service.core.booking.type;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.net.URI;
import java.util.UUID;

@Immutable
@Data
@Entity
@Table(name = "booking_type")
public class BookingType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "map_image_uri")
    private URI mapImageUri;

    @Column(name = "info_image_uri")
    private URI infoImageUri;
}
