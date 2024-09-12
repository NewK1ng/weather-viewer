package dao;

import model.entities.Location;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class LocationDAO {

    public void save(Location location) throws Error {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();
                session.persist(location);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new Error("Something went wrong with database when trying to save location");
            }
        }
    }

    public List<Location> findAllByUserId(int userId) throws Error {
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
                throw new Error("Something went wrong with database when trying to find users locations");
            }
        }
    }

}
