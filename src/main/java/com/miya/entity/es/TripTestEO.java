package com.miya.entity.es;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.List;

@NoArgsConstructor
@Data
@Document(indexName = "trip_test")
@Setting(shards = 1, replicas = 0)
public class TripTestEO {

    @JsonProperty("prop")
    private PropDTO prop = new PropDTO();

    @JsonProperty("parent_prop")
    private ParentPropDTO parentProp = new ParentPropDTO();

    @JsonProperty("ext_info")
    private ExtInfoDTO extInfo = new ExtInfoDTO();

    @JsonProperty("transl")
    private TranslDTO transl = new TranslDTO();

    @NoArgsConstructor
    @Data
    public static class PropDTO {
        @JsonProperty("poi_code")
        private Integer poiCode = 123;

        @JsonProperty("hash_key")
        private String hashKey = "12@37434@BGGABGGD@BG";

        @JsonProperty("poi_type")
        private Integer poiType = 12;

        @JsonProperty("geo")
        private GeoDTO geo = new GeoDTO(42.232, 25.2323);


        @JsonProperty("geoId")
        private Integer geoId = 37434;


        @JsonProperty("location_code")
        private String locationCode = "BGGABGGD";


        @JsonProperty("country_code")
        private String countryCode = "BG";


        @JsonProperty("weight_trip")
        private Integer weightTrip = 0;


        @JsonProperty("weight_outbound")
        private Integer weightOutbound = 0;


        @JsonProperty("weight_trainpal")
        private Integer weightTrainpal = 0;


        @JsonProperty("trip_search_hot")
        private Integer tripSearchHot = 0;

        @NoArgsConstructor
        @AllArgsConstructor
        @Data
        public static class GeoDTO {
            @JsonProperty("lat")
            private Double lat;
            @JsonProperty("lon")
            private Double lon;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ParentPropDTO {
        @JsonProperty("parent_geo_id")
        private String parentGeoId = "37434";
    }

    @NoArgsConstructor
    @Data
    public static class ExtInfoDTO {
        @JsonProperty("tags")
        private List<String> tags = Lists.newArrayList("BG");
    }

    @NoArgsConstructor
    @Data
    public static class TranslDTO {
        @JsonProperty("ngram_all")
        private List<String> ngramAll = Lists.newArrayList("gabrovo", "general", "derozhinski");


        @JsonProperty("edge_ngram_all")
        private List<String> edgeNgramAll = Lists.newArrayList("gabrovo", "general", "derozhinski");


        @JsonProperty("edge_ngram_head")
        private String edgeNgramHead = "gabrovo general derozhinski";


        @JsonProperty("exact_match")
        private List<String> exactMatch = Lists.newArrayList("Gabrovo General Derozhinski", "Gabrovo General Derozhinski", "gabrovo general derozhinski");

        @JsonProperty("raw")
        private String raw = "Gabrovo General Derozhinski";


        @JsonProperty("token")
        private String token = "gabrovo general derozhinski";
        @JsonProperty("tokens")
        private List<String> tokens = Lists.newArrayList("gabrovo", "general", "derozhinski");

        @JsonProperty("locales")
        private List<String> locales = Lists.newArrayList("en_us", "local_name");
    }
}
