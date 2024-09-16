package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Error;
import model.entities.Location;
import model.entities.Sessions;
import model.entities.Users;
import org.thymeleaf.context.Context;
import service.LocationService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/track-location")
public class TrackLocationServlet extends HttpServlet {

    private final LocationService locationService = new LocationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nameParam = req.getParameter("name");
        String latitudeParam = req.getParameter("latitude");
        String longitudeParam = req.getParameter("longitude");

        Context context = (Context) req.getAttribute("context");
        Sessions sessions = (Sessions) context.getVariable("sessions");
        Users user = sessions.getUser();

        Location location = new Location(nameParam, user, new BigDecimal(latitudeParam), new BigDecimal(longitudeParam));

        try {
            locationService.save(location);
        } catch (Error e) {
            throw new RuntimeException(e);
        }

        resp.sendRedirect("/");

    }
}
