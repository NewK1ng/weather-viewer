package service;

import model.Error;
import model.dto.LocationDTO;
import model.dto.LocationWeatherDTO;
import util.HttpClientUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class LocationWeatherService {

    public List<LocationWeatherDTO> findByLocationList(List<LocationDTO> locationDTOList) throws Error {
        URI uri;
        List<LocationWeatherDTO> locationWeatherDTOList = new ArrayList<>();

        for (LocationDTO locationDTO : locationDTOList) {
            try {
                uri = new URI("https://api.openweathermap.org/data/2.5/weather?lat=" + locationDTO.getLatitude() +
                        "&lon=" + locationDTO.getLongitude() + "&units=metric&appid=" + System.getenv("APP_ID") + "&lang=en");
                try {
                    String response = HttpClientUtils.sendGetRequest(uri);

                    LocationWeatherDTO locationWeatherDTO = HttpClientUtils.deserializeJsonToObject(response, LocationWeatherDTO.class);
                    toLocationWeatherDTO(locationDTO, locationWeatherDTO);
                    locationWeatherDTOList.add(locationWeatherDTO);
                } catch (IOException | InterruptedException e) {
                    throw new Error("Something went wrong when trying to search locations");
                }
            } catch (URISyntaxException e) {
                throw new Error("Provide a valid location name");
            }
        }
        return locationWeatherDTOList;
    }

    private void toLocationWeatherDTO(LocationDTO locationDTO, LocationWeatherDTO locationWeatherDTO) {
        locationWeatherDTO.setName(locationDTO.getName());
        locationWeatherDTO.setLatitude(locationDTO.getLatitude());
        locationWeatherDTO.setLongitude(locationDTO.getLongitude());
        locationWeatherDTO.setState(locationDTO.getState());
    }
}
