package servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CustomException;
import model.entities.Sessions;
import org.thymeleaf.context.Context;
import service.SessionsHandlerService;

import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Context context = (Context) req.getAttribute("context");
        Sessions sessions = (Sessions) context.getVariable("sessions");

        SessionsHandlerService sessionsHandlerService = new SessionsHandlerService();

        if (sessions != null) {
            try {
                sessionsHandlerService.delete(sessions);
            } catch (CustomException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        req.getSession().invalidate();

        resp.sendRedirect("/");
    }
}
