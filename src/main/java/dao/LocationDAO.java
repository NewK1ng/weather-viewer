package dao;

import jakarta.servlet.ServletException;
import model.entities.Location;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import util.HibernateUtil;

import java.util.List;

public class LocationDAO {

    public void save(Location location) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();
                session.persist(location);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException("Something went wrong with database when trying to save location - " + location.getName() +
                        ", for user - " + location.getUser().getLogin(),e);
            }
        }
    }

    public void delete(Location location) throws Exception {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();
                session.remove(location);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new Exception("Something went wrong with database when trying to delete location");
            }
        }
    }

    public List<Location> findAllByUserId(Long userId) throws Exception {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();

                List<Location> locations = session.createQuery("FROM Location l WHERE l.user.id = :userId", Location.class)
                        .setParameter("userId", userId)
                        .getResultList();

                session.getTransaction().commit();

                return locations;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new Exception("Something went wrong with database when trying to find users locations");
            }
        }
    }
}
