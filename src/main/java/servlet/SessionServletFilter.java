package servlet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Error;
import model.Sessions;
import model.Users;
import service.SessionService;

import java.io.IOException;
import java.util.UUID;

@WebFilter(urlPatterns = "/*")
public class SessionServletFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                    SessionService sessionService = new SessionService();
                    try {
                        Sessions sessions = sessionService.findById(UUID.fromString(cookie.getValue()));
                        if(sessions != null) {
                            req.getSession().setAttribute("sessions", sessions);
                        }
                    } catch (Error e) {
                        req.getSession().setAttribute("sessions", null);
                    }
                }
            }
        }

        chain.doFilter(req, res);
    }


}
