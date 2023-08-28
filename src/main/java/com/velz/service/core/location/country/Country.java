package com.velz.service.core.location.country;

import jakarta.persistence.*;
import lombok.Data;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.hibernate.annotations.Immutable;

import java.net.URI;
import java.util.UUID;

@Immutable
@Data
@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "alpha3_code")
    private String alpha3Code;

    @Column(name = "calling_code")
    private Integer callingCode;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "description")
    private String description;

    @Column(name = "boundary")
    private Polygon<G2D> boundary;

    @Column(name = "flag_image_uri")
    private URI flagImageUri;

    @Column(name = "main_image_uri")
    private URI mainImageUri;
}
