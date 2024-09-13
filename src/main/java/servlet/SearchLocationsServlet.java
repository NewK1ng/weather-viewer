package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Error;
import model.dto.LocationDTO;
import model.dto.LocationWeatherDTO;
import org.thymeleaf.context.Context;
import service.LocationsService;
import service.WeatherService;
import util.ThymeleafUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/location")
public class SearchLocationsServlet extends HttpServlet {

    private final LocationsService locationsService = new LocationsService();
    private final WeatherService weatherService = new WeatherService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String locationParam = req.getParameter("location");

        if (locationParam != null && !locationParam.isBlank()) {
            try {
                List<LocationDTO> locations = locationsService.findByName(locationParam);
                List<LocationWeatherDTO> locationsWeather = weatherService.findByLocationList(locations);

                Context context = new Context();
                context.setVariable("locationsWeather", locationsWeather);

                ThymeleafUtils.getTemplateEngine().process("search-locations", context, resp.getWriter());

            } catch (Error e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            resp.sendRedirect("/");
        }

    }
}
