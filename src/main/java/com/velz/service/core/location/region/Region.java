package com.velz.service.core.location.region;

import com.fasterxml.jackson.annotation.*;
import com.velz.service.core.location.BoundaryType;
import com.velz.service.core.location.country.Country;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.hibernate.annotations.Immutable;

import java.net.URI;
import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Immutable
@Data
@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("country_id")
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "name")
    private String name;

    @Column(name = "tz_identifier")
    private String tzIdentifier;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @Column(name = "boundary")
    private MultiPolygon<G2D> boundary;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "boundary_type")
    private BoundaryType boundaryType;

    @Column(name = "main_image_uri")
    private URI mainImageUri;
}
