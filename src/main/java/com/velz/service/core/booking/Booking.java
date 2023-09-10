package com.velz.service.core.booking;

import com.fasterxml.jackson.databind.JsonNode;
import com.velz.service.core.booking.type.BookingType;
import com.velz.service.core.location.area.Area;
import com.velz.service.core.location.country.Country;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import java.net.URI;
import java.util.UUID;

@Immutable
@Data
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_type_id")
    private BookingType type;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "coordinate")
    private Point<G2D> coordinate;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @Type(JsonType.class)
    @Column(name = "opening_times")
    private JsonNode openingTimes;

    @Column(name = "website_uri")
    private URI websiteUri;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_country_id")
    private Country currencyCountry;

    @Column(name = "price_per_person")
    private Long pricePerPerson;
}
