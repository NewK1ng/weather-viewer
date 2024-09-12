package servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.entities.Users;
import org.thymeleaf.context.Context;
import service.auth.AuthenticationService;
import service.SessionService;
import util.ThymeleafUtils;

import java.io.IOException;

@WebServlet(urlPatterns = "/sign-in")
public class AuthenticationServlet extends HttpServlet {

    private final AuthenticationService authenticationService = new AuthenticationService();
    private final SessionService sessionService = new SessionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("sessions") != null) {
            resp.sendRedirect("/");
        }

        ThymeleafUtils.getTemplateEngine().process("sign-in", new Context(), resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginParam = req.getParameter("login");
        String passwordParam = req.getParameter("password");

        Context context = new Context();

        try {
            Users user = authenticationService.signIn(loginParam, passwordParam);

            String sessionId = String.valueOf(sessionService.create(user));
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setMaxAge(60 * 60 * 24 * 30);
            resp.addCookie(cookie);

            resp.sendRedirect("/");
        } catch (Exception e) {
            context.setVariable("error", e.getMessage());
        }

        ThymeleafUtils.getTemplateEngine().process("sign-in", context, resp.getWriter());
    }
}
