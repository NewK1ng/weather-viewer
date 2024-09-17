package service;

import com.fasterxml.jackson.core.type.TypeReference;
import dao.LocationDAO;
import model.dto.LocationDTO;
import model.entities.Location;
import org.apache.hc.core5.net.URIBuilder;
import util.HttpClientUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationService {

    private final LocationDAO locationDAO = new LocationDAO();

    public List<LocationDTO> findByName(String location) {
        try {
            URI uri = new URIBuilder("http://api.openweathermap.org/geo/1.0/direct")
                    .addParameter("q", location)
                    .addParameter("limit","5")
                    .addParameter("appid", System.getenv("APP_ID"))
                    .build();

            String jsonResponse = HttpClientUtils.sendGetRequest(uri);
            return HttpClientUtils.deserializeJsonToList(jsonResponse, new TypeReference<>() {
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LocationDTO> findAllByUserId(Long userId) {
        Optional<List<Location>> locationListOpt = locationDAO.findAllByUserId(userId);

        if (locationListOpt.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Location> locationList = locationListOpt.get();
            return toLocationDTOList(locationList);
        }
    }

    public void delete(Location location) {
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
