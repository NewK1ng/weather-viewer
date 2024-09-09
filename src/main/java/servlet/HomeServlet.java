package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Users;
import org.thymeleaf.context.Context;
import util.ThymeleafUtils;

import java.io.IOException;

@WebServlet(urlPatterns = "/")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        Users user = (Users) req.getSession().getAttribute("user");

        Context context = new Context();

        if (user != null) {
            context.setVariable("user", user);
        }

        ThymeleafUtils.getTemplateEngine().process("home", context, resp.getWriter());

    }

}
