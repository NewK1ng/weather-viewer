package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Error;
import model.dto.LocationDTO;
import org.thymeleaf.context.Context;
import service.SearchLocationsService;
import util.ThymeleafUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/location")
public class SearchLocationsServlet extends HttpServlet {

    SearchLocationsService searchLocationsService = new SearchLocationsService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String locationParam = req.getParameter("location");

        if (locationParam != null && !locationParam.isBlank()) {
            try {
                List<LocationDTO> locations = searchLocationsService.search(locationParam);

                for (LocationDTO location : locations) {
                    System.out.println(location);
                }

                Context context = new Context();
                context.setVariable("locations", locations);
                ThymeleafUtils.getTemplateEngine().process("search-locations", context, resp.getWriter());
            } catch (Error e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            resp.sendRedirect("/");
        }

    }
}
