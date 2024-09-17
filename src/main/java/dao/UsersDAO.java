package dao;

import model.CustomException;
import model.entities.Users;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.Optional;

public class UsersDAO {

    public void save(Users users) throws CustomException {
        try (Session session = HibernateUtil.getCurrentSession()) {
            try {
                session.beginTransaction();

                session.persist(users);

                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new CustomException("Login already exists");
            }
        }
    }

    public Optional<Users> findByLogin(String login) throws CustomException {
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
                throw new CustomException("Something went wrong with database when trying to find user");
            }
        }
    }

}
