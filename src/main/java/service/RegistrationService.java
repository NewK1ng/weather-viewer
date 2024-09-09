package service;

import dao.UsersDAO;
import model.Error;
import model.Users;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class RegistrationService {

    private final UsersDAO userDAO = new UsersDAO();

    public List<String> validateInputs(String loginParam, String passwordParam, String confirmPasswordParam) {

        List<String> errors = new ArrayList<>();

        if (loginParam == null || !loginParam.matches("^[a-zA-Z0-9]{3,}$")) {
            errors.add("Login must be at least 3 characters long and contain only latin letters and numbers");
        }

        if (passwordParam == null || !passwordParam.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            errors.add("Password must be at least 8 characters that include at least 1 uppercase letter, 1 lowercase letter and 1 number");
        } else if (!passwordParam.equals(confirmPasswordParam)) {
            errors.add("Passwords do not match");
        }

        return errors;
    }

    public void createUser(String loginParam, String passwordParam) throws Error {

        String hashedPassword = hashPassword(passwordParam);
        Users user = new Users(loginParam, hashedPassword);

        try {
            userDAO.save(user);
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
