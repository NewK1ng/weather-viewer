package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Users;
import org.thymeleaf.context.Context;
import service.AuthenticationService;
import service.SessionService;
import util.ThymeleafUtils;

import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/sign-in")
public class AuthenticationServlet extends HttpServlet {

    private final AuthenticationService authenticationService = new AuthenticationService();
    private final SessionService sessionService = new SessionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ThymeleafUtils.getTemplateEngine().process("sign-in", new Context(), resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String loginParam = req.getParameter("login");
        String passwordParam = req.getParameter("password");

        Context context = new Context();

        try {
            Users user = authenticationService.signIn(loginParam, passwordParam);

            String sessionId = String.valueOf(sessionService.createSession(user));
            Cookie cookie =  new Cookie("sessionId", sessionId);
            cookie.setMaxAge(60 * 60 * 24 * 30);
            resp.addCookie(cookie);

            resp.sendRedirect("/");
        } catch (Exception e) {
            context.setVariable("error", e.getMessage());
        }

        ThymeleafUtils.getTemplateEngine().process("sign-in", context, resp.getWriter());
    }
}
