package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Error;
import model.dto.LocationDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SearchLocationsService {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public List<LocationDTO> search(String location) throws Error {
        URI uri;

        try {
            uri = new URI("http://api.openweathermap.org/geo/1.0/direct?q=" + location + "&limit=5&appid=" + System.getenv("APP_ID"));
        } catch (URISyntaxException e) {
            throw new Error("Provide a valid location name");
        }

        HttpRequest request = HttpRequest.newBuilder(uri).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new Error("Something went wrong when trying to search locations");
        }

        return deserializeJsonToLocationDTOList(response.body());
    }

    private List<LocationDTO> deserializeJsonToLocationDTOList(String json) throws Error {
        try {
            return objectMapper.readValue(json, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new Error("Something went wrong when trying to deserialize response from API");
        }
    }
}
