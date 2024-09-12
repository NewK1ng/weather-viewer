package servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Error;
import org.thymeleaf.context.Context;
import service.auth.RegistrationService;
import util.ThymeleafUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {

    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ThymeleafUtils.getTemplateEngine().process("registration", new Context(), resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginParam = req.getParameter("login");
        String passwordParam = req.getParameter("password");
        String confirmPasswordParam = req.getParameter("confirmPassword");

        Context context = new Context();

        List<String> errors = registrationService.validateInputs(loginParam, passwordParam, confirmPasswordParam);

        if (errors.isEmpty()) {
            try {
                registrationService.createUser(loginParam, passwordParam);
                resp.sendRedirect("/sign-in");
                return;
            } catch (Error e) {
                errors.add(e.getMessage());
            }
        }

        context.setVariable("errors", errors);

        ThymeleafUtils.getTemplateEngine().process("registration", context, resp.getWriter());
    }
}
