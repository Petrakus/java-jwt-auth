package com.jwtauth.example.dao;

import com.jwtauth.example.model.User;
import com.jwtauth.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

public class UserDao {

    public boolean save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.save(user);
            return true;
        } catch (ConstraintViolationException e) {
            System.err.println(e.getMessage());
            return false;
        }

    }
}
