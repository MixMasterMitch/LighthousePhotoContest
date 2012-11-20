package main.java.com.uwlighthouse.photocontest.hibernate;

import main.java.com.uwlighthouse.photocontest.databaseobjects.Picture;
import main.java.com.uwlighthouse.photocontest.databaseobjects.User;
import main.java.com.uwlighthouse.photocontest.databaseobjects.Vote;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple class for interfacing with Hibernate.
 */
public class HibernateUtil {

	private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

	private static final SessionFactory sessionFactory;
	static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	sessionFactory =  new AnnotationConfiguration()
				.addAnnotatedClass(User.class)
				.addAnnotatedClass(Picture.class)
				.addAnnotatedClass(Vote.class)
				.configure()
				.buildSessionFactory();
 
        }
        catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
	}

	/**
	 * @return Current {@link Session} for the {@link #sessionFactory}.
	 */
	public static Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Begins a {@link Transaction} for the current {@link Session}.
	 */
	public static void beginTransaction() {
		getSession().beginTransaction();
	}

	/**
	 * Commits the {@link Transaction} of the current {@link Session}.
	 */
	public static void commitTransaction() {
		getSession().getTransaction().commit();
	}

	/**
	 * Rolls back the current {@link Transaction} of the current {@link Session}.
	 */
	public static void rollbackTransaction() {
		getSession().getTransaction().rollback();
	}

	/**
	 * Closes the current {@link Session} after first committing any open transactions.
	 * If the transaction doesn't get closed, then you will get table locks due to the open transactions.
	 */
	public static void closeSession() {
		if (getSession().getTransaction().isActive()) {
			try {
				commitTransaction();
			} catch (HibernateException e) {
				rollbackTransaction();
			}
		}
		getSession().close();
	}

	/**
	 * @see EffectiveJava#Enforce_noninstantiability
	 */
	private HibernateUtil() {}

}