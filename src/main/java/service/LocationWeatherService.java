package service;

import model.dto.LocationDTO;
import model.dto.LocationWeatherDTO;
import org.apache.hc.core5.net.URIBuilder;
import util.HttpClientUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class LocationWeatherService {

    public List<LocationWeatherDTO> findByLocationList(List<LocationDTO> locationDTOList) {
        List<LocationWeatherDTO> locationWeatherDTOList = new ArrayList<>();

        for (LocationDTO locationDTO : locationDTOList) {
            try {
                URI uri = new URIBuilder("https://api.openweathermap.org/data/2.5/weather")
                        .addParameter("lat", String.valueOf(locationDTO.getLatitude()))
                        .addParameter("lon", String.valueOf(locationDTO.getLongitude()))
                        .addParameter("units", "metric")
                        .addParameter("appid", System.getenv("APP_ID"))
                        .addParameter("lang", "en")
                        .build();

                String jsonResponse = HttpClientUtils.sendGetRequest(uri);

                LocationWeatherDTO locationWeatherDTO = HttpClientUtils.deserializeJsonToObject(jsonResponse, LocationWeatherDTO.class);
                locationWeatherDTO.setLocation(locationDTO);

                locationWeatherDTOList.add(locationWeatherDTO);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return locationWeatherDTOList;
    }
}
