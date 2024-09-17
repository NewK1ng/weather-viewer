package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.LocationDTO;
import model.dto.LocationWeatherDTO;
import org.thymeleaf.context.Context;
import service.LocationService;
import service.LocationWeatherService;
import util.ThymeleafUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/location")
public class SearchLocationsServlet extends HttpServlet {

    private final LocationService locationService = new LocationService();
    private final LocationWeatherService locationWeatherService = new LocationWeatherService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String locationParam = req.getParameter("location");
        Context context = (Context) req.getAttribute("context");

        if (locationParam != null && !locationParam.isBlank()) {
            try {
                List<LocationDTO> locations = locationService.findByName(locationParam);
                List<LocationWeatherDTO> locationsWeather = locationWeatherService.findByLocationList(locations);

                context.setVariable("locationsWeather", locationsWeather);

                ThymeleafUtils.getTemplateEngine().process("search-locations", context, resp.getWriter());

            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            resp.sendRedirect("/");
        }

    }
}
