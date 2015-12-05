package com.chujian.minaUtil;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author qin
 * 单一实例的factory
 */
public class HibernateSessionFactoryUtil {

	/** The single instance of hibernate SessionFactory */
	private static org.hibernate.SessionFactory sessionFactory;

	static {
		try {

			Configuration configuration = new Configuration().configure();

			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
					.applySettings(configuration.getProperties())
					.buildServiceRegistry();

			sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		} catch (Throwable E) {
			throw new ExceptionInInitializerError(E);
		}
	}

	/**
	 * @return
	 */
	public static SessionFactory currentSessionFactory() {

		return sessionFactory;
	}

	/**
	 * Default constructor.
	 */
	private HibernateSessionFactoryUtil() {
	}

}
