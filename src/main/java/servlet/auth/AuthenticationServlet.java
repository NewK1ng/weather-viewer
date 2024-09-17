package servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.entities.Sessions;
import model.entities.Users;
import org.thymeleaf.context.Context;
import service.auth.AuthenticationService;
import service.SessionsHandlerService;
import util.ThymeleafUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(urlPatterns = "/sign-in")
public class AuthenticationServlet extends HttpServlet {

    private final AuthenticationService authenticationService = new AuthenticationService();
    private final SessionsHandlerService sessionsHandlerService = new SessionsHandlerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = (Context) req.getAttribute("context");
        Sessions sessions = (Sessions) context.getVariable("sessions");

        if (sessions != null) {
            resp.sendRedirect("/");
        }

        ThymeleafUtils.getTemplateEngine().process("sign-in", new Context(), resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginParam = req.getParameter("login");
        String passwordParam = req.getParameter("password");

        Context context = (Context) req.getAttribute("context");

        try {
            Users user = authenticationService.signIn(loginParam, passwordParam);
            Optional<UUID> sessionIdOpt = sessionsHandlerService.create(user);

            if (sessionIdOpt.isPresent()) {
                String sessionId = String.valueOf(sessionIdOpt.get());
                Cookie cookie = new Cookie("sessionId", sessionId);
                cookie.setMaxAge(60 * 60 * 24 * 30);
                resp.addCookie(cookie);
                resp.sendRedirect("/");
            } else {
                throw new ServletException("Failed to create session");
            }
        } catch (Exception e) {
            context.setVariable("error", e.getMessage());
        }

        ThymeleafUtils.getTemplateEngine().process("sign-in", context, resp.getWriter());
    }
}
