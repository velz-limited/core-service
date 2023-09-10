package com.velz.service.core.location.area;

import com.fasterxml.jackson.databind.JsonNode;
import com.velz.service.core.location.region.Region;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import java.net.URI;
import java.util.UUID;

@Immutable
@Data
@Entity
@Table(name = "area")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "centre_coordinate")
    private Point<G2D> centreCoordinate;

    @Column(name = "description")
    private String description;

    @Column(name = "morning_description")
    private String morningDescription;

    @Column(name = "afternoon_description")
    private String afternoonDescription;

    @Column(name = "evening_description")
    private String eveningDescription;

    @Column(name = "night_description")
    private String nightDescription;

    @Column(name = "boundary")
    private Polygon<G2D> boundary;

    @Column(name = "main_image_uri")
    private URI mainImageUri;

    @Type(JsonType.class)
    @Column(name = "hashtags")
    private JsonNode hashtags;
}
