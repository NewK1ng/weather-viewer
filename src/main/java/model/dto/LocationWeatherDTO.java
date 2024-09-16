package model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationWeatherDTO {
    @JsonIgnore
    private String name;
    @JsonIgnore
    private BigDecimal latitude;
    @JsonIgnore
    private BigDecimal longitude;
    @JsonIgnore
    private String state;

    @JsonProperty("weather")
    private List<Weather> weather;
    @JsonProperty("main")
    private Main main;
    @JsonProperty("sys")
    private Sys sys;

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
    public static class Sys{
        @JsonProperty("country")
        private String country;
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
