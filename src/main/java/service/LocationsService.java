package service;

import com.fasterxml.jackson.core.type.TypeReference;
import model.Error;
import model.dto.LocationDTO;
import util.HttpClientUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class LocationsService {

    public List<LocationDTO> findByName(String location) throws Error {
        URI uri;

        try {
            uri = new URI("http://api.openweathermap.org/geo/1.0/direct?q=" + location + "&limit=5&appid=" + System.getenv("APP_ID"));
        } catch (URISyntaxException e) {
            throw new Error("Provide a valid location name");
        }

        try {
            String response = HttpClientUtils.sendGetRequest(uri);
            return HttpClientUtils.deserializeJsonToList(response, new TypeReference<>() {});
        } catch (IOException | InterruptedException e) {
            throw new Error("Something went wrong when trying to search locations");
        }
    }
}
