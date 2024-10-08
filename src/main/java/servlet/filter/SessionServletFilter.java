package servlet.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entities.Sessions;
import org.thymeleaf.context.Context;
import service.SessionsHandlerService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebFilter(urlPatterns = "/*")
public class SessionServletFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        Cookie[] cookies = req.getCookies();

        Context context = new Context();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                    SessionsHandlerService sessionsHandlerService = new SessionsHandlerService();
                    try {
                        Optional<Sessions> sessionsOpt = sessionsHandlerService.findById(UUID.fromString(cookie.getValue()));
                        if (sessionsOpt.isPresent()) {
                            Sessions sessions = sessionsOpt.get();
                            context.setVariable("sessions", sessions);
                        } else {
                            req.getSession().invalidate();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        req.setAttribute("context", context);

        chain.doFilter(req, res);
    }

}
