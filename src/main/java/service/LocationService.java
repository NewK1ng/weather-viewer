package service;

import com.fasterxml.jackson.core.type.TypeReference;
import dao.LocationDAO;
import model.CustomException;
import model.dto.LocationDTO;
import model.entities.Location;
import util.HttpClientUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class LocationService {

    private final LocationDAO locationDAO = new LocationDAO();

    public List<LocationDTO> findByName(String location) throws Exception {
        URI uri;

        try {
            uri = new URI("http://api.openweathermap.org/geo/1.0/direct?q=" + location + "&limit=5&appid=" + System.getenv("APP_ID"));
        } catch (URISyntaxException e) {
            throw new CustomException("Provide a valid location name");
        }

        try {
            String response = HttpClientUtils.sendGetRequest(uri);
            return HttpClientUtils.deserializeJsonToList(response, new TypeReference<>() {});
        } catch (IOException | InterruptedException e) {
            throw new CustomException("Something went wrong when trying to search locations");
        }
    }

    public List<LocationDTO> findAllByUserId(Long userId) throws Exception {
        List<Location> location = locationDAO.findAllByUserId(userId);
        return toLocationDTOList(location);
    }

    public void delete(Location location) throws Exception {
        locationDAO.delete(location);
    }

    public void save(Location location) {
        locationDAO.save(location);
    }

    private List<LocationDTO> toLocationDTOList(List<Location> locationList) {
        List<LocationDTO> locationDTOList = new ArrayList<>();

        for (Location location : locationList) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setLatitude(location.getLatitude());
            locationDTO.setLongitude(location.getLongitude());
            locationDTO.setName(location.getName());
            locationDTOList.add(locationDTO);
        }
        return locationDTOList;
    }
}
