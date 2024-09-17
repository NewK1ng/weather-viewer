package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import model.entities.Location;
import model.entities.Sessions;
import model.entities.Users;
import org.hibernate.exception.ConstraintViolationException;
import org.thymeleaf.context.Context;
import service.LocationService;

import java.io.IOException;
import java.math.BigDecimal;


@Slf4j
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

        if (nameParam != null && latitudeParam != null && longitudeParam != null && sessions != null) {

            Users user = sessions.getUser();
            BigDecimal latitude = new BigDecimal(latitudeParam);
            BigDecimal longitude = new BigDecimal(longitudeParam);

            Location location = new Location(nameParam, user, latitude, longitude);

            try {
                locationService.save(location);
            } catch (Exception e) {
                if (e.getCause() instanceof ConstraintViolationException) {
                    resp.sendRedirect("/");
                    return;
                }
                throw new RuntimeException(e);
            }
        }

        resp.sendRedirect("/");
    }
}
