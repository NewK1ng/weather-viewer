package dao;

import model.CustomException;
import model.entities.Sessions;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.Optional;
import java.util.UUID;

public class SessionsDAO {

    public void save(Sessions sessions) throws CustomException {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();
                session.persist(sessions);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new CustomException("Session already exists");
            }
        }
    }

    public void update(Sessions sessions) throws CustomException {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();
                session.update(sessions);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new CustomException("Something went wrong with database when trying to update session");
            }
        }
    }

    public void delete(Sessions sessions) throws CustomException {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();
                session.remove(sessions);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new CustomException("Something went wrong with database when trying to delete session");
            }
        }
    }

    public Optional<Sessions> findById(UUID id) throws CustomException {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();
                Sessions sessions = session.createQuery("FROM Sessions s JOIN FETCH s.user u WHERE s.id = :id", Sessions.class)
                        .setParameter("id", id)
                        .getSingleResultOrNull();
                session.getTransaction().commit();

                return Optional.ofNullable(sessions);
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new CustomException("Something went wrong with database when trying to find session");
            }
        }
    }

}
