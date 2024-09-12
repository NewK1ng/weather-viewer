package dao;

import model.Error;
import model.entities.Users;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.Optional;

public class UsersDAO {

    public void save(Users users) throws Error {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();

                session.persist(users);

                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new Error("Login already exists");
            }
        }
    }

    public Optional<Users> findByLogin(String login) throws Error {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {

                session.beginTransaction();

                Users userOpt = session.createQuery("FROM Users u WHERE u.login = :login", Users.class)
                        .setParameter("login", login)
                        .getSingleResult();

                session.getTransaction().commit();

                return Optional.ofNullable(userOpt);
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new Error("Something went wrong with database when trying to find user");
            }
        }
    }

}
