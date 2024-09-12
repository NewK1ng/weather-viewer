package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entities.Sessions;
import org.thymeleaf.context.Context;
import util.ThymeleafUtils;

import java.io.IOException;

@WebServlet(urlPatterns = "/")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Sessions sessions = (Sessions) req.getSession().getAttribute("sessions");

        Context context = new Context();

        if (sessions != null) {
            context.setVariable("sessions", sessions);
        }

        ThymeleafUtils.getTemplateEngine().process("home", context, resp.getWriter());
    }

}
