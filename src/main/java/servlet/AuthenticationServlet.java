package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Session;
import model.Users;
import org.thymeleaf.context.Context;
import service.AuthenticationService;
import util.ThymeleafUtils;

import java.io.IOException;

@WebServlet(urlPatterns = "/sign-in")
public class AuthenticationServlet extends HttpServlet {

    private final AuthenticationService authenticationService = new AuthenticationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null) {
            resp.sendRedirect("/");
        } else {
            ThymeleafUtils.getTemplateEngine().process("sign-in", new Context(), resp.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session != null) {
            resp.sendRedirect("/");
        }

        String loginParam = req.getParameter("login");
        String passwordParam = req.getParameter("password");

        Context context = new Context();

        try {
            Users user = authenticationService.signIn(loginParam, passwordParam);

            session = req.getSession(true);
            String sessionId = session.getId();
            session.setAttribute("user", user);

            resp.sendRedirect("/");

        } catch (Exception e) {
            context.setVariable("error", e.getMessage());
        }

        ThymeleafUtils.getTemplateEngine().process("sign-in", context, resp.getWriter());
    }
}
