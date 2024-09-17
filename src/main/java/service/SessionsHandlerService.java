package service;

import dao.SessionsDAO;
import model.entities.Sessions;
import model.entities.Users;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionsHandlerService {
    private final SessionsDAO sessionsDAO = new SessionsDAO();
    private final static int SESSION_TIMEOUT_MINUTES = 30;
    private final static int ATTEMPTS_TO_CREATE_SESSION = 2;

    public Optional<UUID> create(Users user) {
        for (int i = 0; i < ATTEMPTS_TO_CREATE_SESSION; i++) {
            try {
                UUID sessionId = UUID.randomUUID();
                LocalDateTime expiresAtTime = LocalDateTime.now().plusMinutes(SESSION_TIMEOUT_MINUTES);

                Sessions sessions = new Sessions(sessionId, user, expiresAtTime);
                sessionsDAO.save(sessions);

                return Optional.of(sessionId);
            } catch (Exception e) {
                i++;
                if (i == ATTEMPTS_TO_CREATE_SESSION) {
                    throw new RuntimeException("Something went wrong with database when trying to create session",e);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Sessions> findById(UUID sessionId) {
       Optional<Sessions> sessionsOpt = sessionsDAO.findById(sessionId);

       if(sessionsOpt.isPresent()) {
           Sessions sessions = sessionsOpt.get();

           if(!isExpired(sessions)) {
               updateExpiresAt(sessions);
               return sessionsOpt;
           }
       }

       return Optional.empty();
    }

    public void delete(Sessions sessions) {
        sessionsDAO.delete(sessions);
    }

    private void updateExpiresAt(Sessions sessions) {
        sessions.setExpiresAt(LocalDateTime.now().plusMinutes(SESSION_TIMEOUT_MINUTES));
        sessionsDAO.update(sessions);
    }

    private boolean isExpired(Sessions sessions) {
        return sessions.getExpiresAt().isBefore(LocalDateTime.now());
    }
}

