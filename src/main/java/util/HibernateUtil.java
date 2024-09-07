package util;

import model.Location;
import model.Session;
import model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    private HibernateUtil() {}

    static {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(Session.class);
        cfg.addAnnotatedClass(Location.class);

        sessionFactory = cfg.buildSessionFactory();
    }

    public static org.hibernate.Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
