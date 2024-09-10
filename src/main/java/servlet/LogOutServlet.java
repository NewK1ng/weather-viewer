package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Error;
import model.Sessions;
import service.SessionService;

import java.io.IOException;

@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Sessions sessions = (Sessions) req.getSession().getAttribute("sessions");

        SessionService sessionService = new SessionService();

        try {
            sessionService.deleteSession(sessions);
        } catch (Error e) {
            throw new RuntimeException(e);
        }

        req.getSession().invalidate();

        resp.sendRedirect("/");
    }
}
