package com.jwtauth.example.dao;

import com.jwtauth.example.model.User;
import com.jwtauth.example.util.CypherUtils;
import com.jwtauth.example.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

public class UserDao {

    private final Logger LOGGER = Logger.getLogger(UserDao.class);

    public boolean save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user.setPassword(CypherUtils.getSHA512SecurePassword(user.getPassword()));
            session.save(user);
            return true;
        } catch (ConstraintViolationException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public User findUserByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> q = session.createQuery("from User where email = :email", User.class);
            q.setParameter("email", email);
            return q.getSingleResult();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public User findUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> q = session.createQuery("from User where id = :id", User.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
