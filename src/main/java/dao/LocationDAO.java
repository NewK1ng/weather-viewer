package dao;

import model.entities.Location;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

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
                        " for user - " + location.getUser().getLogin(), e);
            }
        }
    }

    public void delete(Location location) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();
                session.remove(location);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException("Something went wrong with database when trying to remove location - " + location.getName() +
                        " for user - " + location.getUser().getLogin(), e);
            }
        }
    }

    public Optional<List<Location>> findAllByUserId(Long userId) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();

                List<Location> locations = session.createQuery("FROM Location l WHERE l.user.id = :userId", Location.class)
                        .setParameter("userId", userId)
                        .getResultList();

                session.getTransaction().commit();

                if (locations.isEmpty()) {
                    return Optional.empty();
                } else {
                    return Optional.of(locations);
                }
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException("Something went wrong with database when trying to get all locations for userId - " + userId, e);
            }
        }
    }
}
