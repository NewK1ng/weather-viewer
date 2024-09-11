package service;

import dao.SessionsDAO;
import model.Error;
import model.Sessions;
import model.Users;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionService {

    private final SessionsDAO sessionsDAO = new SessionsDAO();
    private final static int SESSION_TIMEOUT_MINUTES = 30;
    private final static int ATTEMPTS_TO_CREATE_SESSION = 2;

    public UUID create(Users user) throws Error {
        for (int i = 0; i < ATTEMPTS_TO_CREATE_SESSION; i++) {
            try {
                UUID sessionId = UUID.randomUUID();
                LocalDateTime expiresAtTime = LocalDateTime.now().plusMinutes(SESSION_TIMEOUT_MINUTES);

                Sessions sessions = new Sessions(sessionId, user, expiresAtTime);
                sessionsDAO.save(sessions);

                return sessionId;
            } catch (Error e) {
                i++;
                if (i == ATTEMPTS_TO_CREATE_SESSION) {
                    throw new Error("Something went wrong with database when trying to create session");
                }
            }
        }
        return null;
    }

    public Sessions findById(UUID sessionId) throws Error {
       Optional<Sessions> sessionsOpt = sessionsDAO.findById(sessionId);
       if(sessionsOpt.isPresent()) {

           Sessions sessions = sessionsOpt.get();

           if(!isExpired(sessions)) {
               updateExpiresAt(sessions);
               return sessions;
           } else {
               sessionsDAO.delete(sessions);
           }
       }
       return null;
    }

    public void delete(Sessions sessions) throws Error {
        sessionsDAO.delete(sessions);
    }

    private void updateExpiresAt(Sessions sessions) throws Error {
        sessions.setExpiresAt(LocalDateTime.now().plusMinutes(SESSION_TIMEOUT_MINUTES));
        sessionsDAO.update(sessions);
    }

    private boolean isExpired(Sessions sessions) {
        return sessions.getExpiresAt().isBefore(LocalDateTime.now());
    }

}

