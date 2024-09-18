package model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import model.entities.Location;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationWeatherDTO {

    @JsonIgnore
    private LocationDTO location;

    @JsonProperty("weather")
    private List<Weather> weather;
    @JsonProperty("main")
    private Main main;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Weather {
        @JsonProperty("main")
        private String main;
        @JsonProperty("description")
        private String description;
        @JsonProperty("icon")
        private String icon;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Main {
        @JsonProperty("temp")
        private double temp;
        @JsonProperty("temp_min")
        private double temp_min;
        @JsonProperty("temp_max")
        private double temp_max;
        @JsonProperty("humidity")
        private double humidity;
        @JsonProperty("feels_like")
        private double feels_like;
    }
}
