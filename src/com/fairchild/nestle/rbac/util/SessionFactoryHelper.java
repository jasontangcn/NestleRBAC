package com.fairchild.nestle.rbac.util;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.HibernateException;

import com.fairchild.nestle.rbac.Account;
import com.fairchild.nestle.rbac.Privilege;
import com.fairchild.nestle.rbac.Role;

public class SessionFactoryHelper {

	/**
	 * Location of hibernate.cfg.xml file. NOTICE: Location should be on the
	 * classpath as Hibernate uses #resourceAsStream style lookup for its
	 * configuration file. That is place the config file in a Java package - the
	 * default location is the default Java package.<br>
	 * <br>
	 * Examples: <br>
	 * <code>CONFIG_FILE_LOCATION = "/hibernate.conf.xml". 
	 * CONFIG_FILE_LOCATION = "/com/foo/bar/myhiberstuff.conf.xml".</code>
	 */
	// private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";

	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

	// private static final Configuration cfg = new Configuration();
	private static final AnnotationConfiguration anno = new AnnotationConfiguration();
	private static SessionFactory sessionFactory;

	private SessionFactoryHelper() {
	}
	
	public static Session currentSession() throws HibernateException {
		Session session = (Session) threadLocal.get();

		if (null == session) {
			if (null == sessionFactory) {
				try {
					Class[] clazz = { Account.class, Role.class, Privilege.class };
					List<Class> al = Arrays.asList(clazz);
					anno.addAnnotatedClasses(al);
					// anno.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
					sessionFactory = anno.buildSessionFactory();
					// cfg.configure(CONFIG_FILE_LOCATION);
					// sessionFactory = cfg.buildSessionFactory();
				} catch (Exception e) {
					System.err.println("Error Creating SessionFactory.");
					e.printStackTrace();
				}
			}
			session = sessionFactory.openSession();
			threadLocal.set(session);
		}

		return session;
	}

	public static void closeSession() throws HibernateException {
		Session session = threadLocal.get();
		threadLocal.set(null);

		if (null != session) {
			session.close();
		}
	}
}
