package service.auth;

import dao.UsersDAO;
import model.entities.Users;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthenticationService {

    private final UsersDAO usersDAO = new UsersDAO();

    public Users signIn(String login, String password) {
        Optional<Users> userOpt = usersDAO.findByLogin(login);

        if(userOpt.isPresent()) {
            Users user = userOpt.get();
            if(checkPassword(password, user.getPassword())) {
                return user;
            }
        }
        throw new RuntimeException("Login or password is incorrect");
    }

    public boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}