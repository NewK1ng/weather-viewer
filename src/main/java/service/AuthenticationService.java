package service;

import dao.UsersDAO;
import model.Error;
import model.Users;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthenticationService {

    private final UsersDAO usersDAO = new UsersDAO();

    public Users signIn(String login, String password) throws Exception {

        Optional<Users> userOpt = usersDAO.get(login);

        if(userOpt.isPresent()) {
            Users user = userOpt.get();

            if(checkPassword(password, user.getPassword())) {
                return user;
            } else {
                throw new Error("Login or password is incorrect");
            }
        } else {
            throw new Error("Login or password is incorrect");
        }

    }

    public boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}