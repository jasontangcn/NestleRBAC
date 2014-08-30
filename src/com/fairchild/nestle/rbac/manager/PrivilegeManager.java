/*
 * Created on May 7, 2005
 * TomHornson(at)hotmail.com
 */
package com.fairchild.nestle.rbac.manager;

import java.util.*;

import org.hibernate.*;

import com.fairchild.nestle.rbac.Privilege;
import com.fairchild.nestle.rbac.util.SessionFactoryHelper;

public class PrivilegeManager {
	/*
	 * Elements of HashSet, TreeSet and LinkedHashSet have specific semantics.
	 * e.g. Elements of HashSet have to override hashCode and equals and Elements
	 * of TreeSet have to implements Compare, so I pick HashMap instead of a
	 * interface Map.
	 */
	public void addPrivileges(HashMap<String, String> name2alias) {
		Session session = null;
		Transaction tx = null;
		try {
			session = SessionFactoryHelper.currentSession();
			tx = session.beginTransaction();

			if ((null != name2alias) && (name2alias.size() > 0)) {
				Set<Map.Entry<String, String>> es = name2alias.entrySet();
				for (Map.Entry<String, String> entry : es) {
					if ((null != entry.getKey()) && (entry.getKey().length() > 0) && (null != entry.getValue()) && (entry.getValue().length() > 0)) {
						Privilege p = new Privilege();
						p.setName(entry.getKey());
						p.setAlias(entry.getValue());
						session.save(p);
					}
				}
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

	public void removePrivileges(int[] privilegeIds) {
		Session session = null;
		Transaction tx = null;
		try {
			session = SessionFactoryHelper.currentSession();
			tx = session.beginTransaction();

			if ((null != privilegeIds) && (privilegeIds.length > 0)) {
				for (int id : privilegeIds) {
					Privilege p = (Privilege) session.load(Privilege.class, new Integer(id));
					session.delete(p);
				}
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

	/*
	 * private void removePrivileges(int[] pids, Session session){ if((null !=
	 * pids) && (pids.length > 0){ for(int id : pids){ Privilege p =
	 * (Privilege)session.load(Privilege.class,new Long(id)); session.delete(p); }
	 * } }
	 */
	public static void main(String[] args) {
		//String[] privs = { "ClassA:MethodA", "ClassA:MethodB", "ClassB:MethodA:int,int", "ClassB:MethodA:float,float" };
		//new PrivilegeManager().addPrivileges(privs);

		/*
		Privilege p1 = new Privilege();
		p1.setName("p1");
		Privilege p2 = new Privilege();
		p2.setName("p2");
		Set<Privilege> privs = new TreeSet<Privilege>();
		privs.add(p1);
		privs.add(p2);
		for (Privilege p : privs) {
			System.out.println(p);
		}
		*/
	}
}
