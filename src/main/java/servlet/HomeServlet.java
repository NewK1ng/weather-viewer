package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.LocationDTO;
import model.dto.LocationWeatherDTO;
import model.entities.Sessions;
import org.thymeleaf.context.Context;
import service.LocationService;
import service.LocationWeatherService;
import util.ThymeleafUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/")
public class HomeServlet extends HttpServlet {

    private final LocationService locationService = new LocationService();
    private final LocationWeatherService locationWeatherService = new LocationWeatherService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Context context = (Context) req.getAttribute("context");
        Sessions sessions = (Sessions) context.getVariable("sessions");

        if (sessions != null) {
            try {
                List<LocationDTO> locationDTOList = locationService.findAllByUserId(sessions.getUser().getId());
                List<LocationWeatherDTO> locationsWeather = locationWeatherService.findByLocationList(locationDTOList);
                context.setVariable("locationsWeather", locationsWeather);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        ThymeleafUtils.getTemplateEngine().process("home", context, resp.getWriter());
    }

}
