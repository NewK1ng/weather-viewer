package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        Context context = new Context();
        ThymeleafUtils.getTemplateEngine().process("sign-in", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String loginParam = req.getParameter("login");
        String passwordParam = req.getParameter("password");

        Context context = new Context();

        try {
            Users user = authenticationService.signIn(loginParam, passwordParam);
            req.getSession().setAttribute("user", user);
            resp.sendRedirect("/");
        } catch (Exception e) {
            context.setVariable("error", e.getMessage());
        }

        ThymeleafUtils.getTemplateEngine().process("sign-in", context, resp.getWriter());
    }
}
