package com.jwtauth.example.dao;

import com.jwtauth.example.model.User;
import com.jwtauth.example.util.CypherUtils;
import com.jwtauth.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UserDao {

    public boolean save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user.setPassword(CypherUtils.getSHA512SecurePassword(user.getPassword()));
            session.save(user);
            return true;
        } catch (ConstraintViolationException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public User findUserByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> root = criteria.from(User.class);
            criteria.select(root).where(builder.equal(root.get("email"), email));
            Query<User> q = session.createQuery(criteria);
            return q.getSingleResult();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public User findUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> root = criteria.from(User.class);
            criteria.select(root).where(builder.equal(root.get("id"), id));
            Query<User> q = session.createQuery(criteria);
            return q.getSingleResult();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
