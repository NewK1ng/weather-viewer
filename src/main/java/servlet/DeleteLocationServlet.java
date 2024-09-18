package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entities.Location;
import model.entities.Sessions;
import model.entities.Users;
import org.thymeleaf.context.Context;
import service.LocationService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(urlPatterns = "/delete-location")
public class DeleteLocationServlet extends HttpServlet {

    private final LocationService locationService = new LocationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String nameParam = req.getParameter("name");
        String latitudeParam = req.getParameter("latitude");
        String longitudeParam = req.getParameter("longitude");
        String countryParam = req.getParameter("country");
        String stateParam = req.getParameter("state");

        try {
            Context context = (Context) req.getAttribute("context");
            Sessions sessions = (Sessions) context.getVariable("sessions");

            Long id = Long.valueOf(idParam);
            Users user = sessions.getUser();
            BigDecimal latitude = new BigDecimal(latitudeParam);
            BigDecimal longitude = new BigDecimal(longitudeParam);

            Location location = new Location(id,nameParam, user, latitude, longitude,countryParam, stateParam);
            locationService.delete(location);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        resp.sendRedirect("/");
    }
}
