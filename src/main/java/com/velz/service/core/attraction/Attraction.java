package com.velz.service.core.attraction;

import com.fasterxml.jackson.databind.JsonNode;
import com.velz.service.core.attraction.type.AttractionType;
import com.velz.service.core.location.area.Area;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Immutable
@Data
@Entity
@Table(name = "attraction")
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attraction_type_id")
    private AttractionType type;

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

    @Column(name = "min_explore_duration")
    private Duration minExploreDuration;

    @Column(name = "max_explore_duration")
    private Duration maxExploreDuration;

    @Column(name = "website_uri")
    private URI websiteUri;

    @Type(JsonType.class)
    @Column(name = "facts")
    private JsonNode facts;

    @Type(JsonType.class)
    @Column(name = "history")
    private JsonNode history;

    @Type(JsonType.class)
    @Column(name = "accessibility")
    private JsonNode accessibility;

    @Type(JsonType.class)
    @Column(name = "recommendations")
    private JsonNode recommendations;

    @Column(name = "main_image_uri")
    private URI mainImageUri;
}
