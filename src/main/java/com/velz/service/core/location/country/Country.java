package com.velz.service.core.location.country;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.velz.service.core.location.BoundaryType;
import jakarta.persistence.*;
import lombok.Data;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.hibernate.annotations.Immutable;

import java.net.URI;
import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @Column(name = "alpha2_code")
    private String alpha2Code;

    @Column(name = "alpha3_code")
    private String alpha3Code;

    @Column(name = "calling_code")
    private Integer callingCode;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @Column(name = "boundary")
    private MultiPolygon<G2D> boundary;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "boundary_type")
    private BoundaryType boundaryType;

    @Column(name = "flag_image_uri")
    private URI flagImageUri;

    @Column(name = "main_image_uri")
    private URI mainImageUri;
}
