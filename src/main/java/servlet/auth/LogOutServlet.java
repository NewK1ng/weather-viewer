package servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Error;
import model.entities.Sessions;
import service.SessionService;

import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Sessions sessions = (Sessions) req.getSession().getAttribute("sessions");
        SessionService sessionService = new SessionService();

        if (sessions != null) {
            try {
                sessionService.delete(sessions);
            } catch (Error e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        req.getSession().invalidate();

        resp.sendRedirect("/");
    }
}
