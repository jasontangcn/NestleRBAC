/*
 * Created on May 18, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.nestle.rbac.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.lang.reflect.*;

public abstract class HibernateTemplate {
	public void templateMethod(Method method, Object[] args) {
		Session session = null;
		Transaction tx = null;
		try {
			session = SessionFactoryHelper.currentSession();
			tx = session.beginTransaction();
			try {
				method.invoke(this, args);
			} catch (Exception e) {
				throw new RuntimeException("Invoking business operation failed.");
			}
			tx.commit();
			session.flush();
		} catch (HibernateException e) {
			e.printStackTrace();
			try {
				if (null != tx)
					tx.rollback();
			} catch (HibernateException ex) {
				ex.printStackTrace();
			}
			throw e;
		} finally {
			try {
				if (null != session)
					session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
	}
}
