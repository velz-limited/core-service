package com.velz.service.core.attraction.type;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.net.URI;
import java.util.UUID;

@Immutable
@Data
@Entity
@Table(name = "attraction_type")
public class AttractionType {

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
