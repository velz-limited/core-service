package com.velz.service.core.location.response;

import lombok.Data;
import org.geolatte.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

@Data
public class FeaturesGeoJsonResponse {

    private String type = "FeatureCollection";
    private String name;
    private Crs crs;
    private List<Feature> features = new ArrayList<>();

    @Data
    public static class Crs {
        private String type;
        private Object properties;
    }

    @Data
    public static class Feature {
        private String type = "Feature";
        private Object properties;
        private Geometry geometry;
    }
}
