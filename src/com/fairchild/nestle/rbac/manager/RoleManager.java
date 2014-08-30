/*
 * Created on May 7, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.nestle.rbac.manager;

import java.util.Set;
import java.util.HashSet;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fairchild.nestle.rbac.RBACException;
import com.fairchild.nestle.rbac.Privilege;
import com.fairchild.nestle.rbac.Role;
import com.fairchild.nestle.rbac.util.SessionFactoryHelper;

public class RoleManager {
	public void addRole(String roleName, int[] parentIds, int[] privilegeIds) {
		Session session = null;
		Transaction tx = null;
		try {
			session = SessionFactoryHelper.currentSession();
			tx = session.beginTransaction();

			if ((null == roleName) || (roleName.length() == 0))
				throw new RBACException("Role name can't be NULL or 0 length string.");

			Role role = new Role();
			role.setName(roleName);

			HashSet<Role> parents = null;
			if ((null != parentIds) && (parentIds.length > 0)) {
				parents = new HashSet<Role>();
				for (int id : parentIds) {
					Role parent = (Role) session.load(Role.class, new Integer(id));
					parents.add(parent);
				}
				role.setParents(parents);
			}

			HashSet<Privilege> privileges = null;
			if ((null != privilegeIds) && (privilegeIds.length > 0)) {
				privileges = new HashSet<Privilege>();
				for (int id : privilegeIds) {
					Privilege privilege = (Privilege) session.load(Privilege.class, new Integer(id));
					privileges.add(privilege);
				}
				role.setPrivileges(privileges);
			}

			session.save(role);
			tx.commit();
			session.flush();
		} catch (HibernateException e) {
			e.printStackTrace();
			try {
				if (null != tx)
					tx.rollback();
			} catch (HibernateException ex) {
			}
		} finally {
			try {
				if (null != session)
					session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
	}

	public void addPrivileges(int roleId, int[] privilegeIds) {
		Session session = null;
		Transaction tx = null;
		try {
			session = SessionFactoryHelper.currentSession();
			tx = session.beginTransaction();

			Role role = (Role) session.load(Role.class, new Integer(roleId));

			HashSet<Privilege> privilegesAdded = null;
			if ((null != privilegeIds) && (privilegeIds.length > 0)) {
				privilegesAdded = new HashSet<Privilege>();
				for (int id : privilegeIds) {
					Privilege privilege = (Privilege)session.load(Privilege.class, new Integer(id));
					privilegesAdded.add(privilege);
				}
			}

			Set<Privilege> privileges = role.getPrivileges();
			/*
			 * Notice: If one role does not have parent, role.getPrivileges() may return NULL.
			 */
			if (null == privileges) {
				role.setPrivileges(privilegesAdded);
			} else {
				privileges.addAll(privilegesAdded);
			}

			tx.commit();
			session.flush();
		} catch (HibernateException e) {
			e.printStackTrace();
			try {
				if (null != tx)
					tx.rollback();
			} catch (HibernateException ex) {

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

	public void removePrivileges(int roleId, int[] privilegeIds) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = SessionFactoryHelper.currentSession();
			tx = session.beginTransaction();

			HashSet<Privilege> privilegesRemoved = null;
			Role role = (Role) session.load(Role.class, new Long(roleId));
			if ((null != privilegeIds) && (privilegeIds.length > 0)) {
				privilegesRemoved = new HashSet<Privilege>();
				for (int id : privilegeIds) {
					/*
					 * If there isn't a privilege whoes id == id, 
					 * a HibernateException(Instance of RuntimeException) will be thrown.
					 */
					Privilege privilege = (Privilege) session.load(Privilege.class, new Integer(id));
					/*
					 * Result of role.getPrivileges() will not be checked whether or not it's NULL.
					 * If it's NULL, NullPointerException will be throwed.
					 */
					if (!role.getPrivileges().remove(privilegesRemoved))
						throw new RBACException("Can't find the privilege to be removed.");
				}
			}
			tx.commit();
			session.flush();
		} catch (Exception e) {
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

	public void addParents(int roleId, int[] parentIds) {
		Session session = null;
		Transaction tx = null;
		try {
			session = SessionFactoryHelper.currentSession();
			tx = session.beginTransaction();

			HashSet<Role> parentsAdded = null;
			Role role = (Role) session.load(Role.class, new Long(roleId));
			if ((null != parentIds) && (parentIds.length > 0)) {
				parentsAdded = new HashSet<Role>();
				for (int id : parentIds) {
					Role parent = (Role) session.load(Role.class, new Integer(id));
					parentsAdded.add(parent);
				}
			}

			Set<Role> parents = role.getParents();
			if (null == parents) {
				role.setParents(parentsAdded);
			} else {
				parents.addAll(parentsAdded);
			}

			tx.commit();
			session.flush();
		} catch (HibernateException e) {
			e.printStackTrace();
			try {
				if (null != tx)
					tx.rollback();
			} catch (HibernateException ex) {

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

	public void removeParents(int roleId, int[] parentIds) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = SessionFactoryHelper.currentSession();
			tx = session.beginTransaction();

			HashSet<Role> parentsRemoved = null;
			Role role = (Role) session.load(Role.class, new Long(roleId));
			if ((null != parentIds) && (parentIds.length > 0)) {
				parentsRemoved = new HashSet<Role>();
				for (int id : parentIds) {
					Role parent = (Role) session.load(Role.class, new Integer(id));
					/*
					 * Result of role.getParents() will not be checked whether or not it's NULL.
					 * If it's NULL, NullPointerException will be thrown.
					 */
					parentsRemoved.add(parent);
				}
			}
			if (!role.getParents().removeAll(parentsRemoved))
				throw new RuntimeException("Can't find the parent to be removed.");

			tx.commit();
			session.flush();
		} catch (Exception e) {
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

	public static void main(String[] args) {
		/*
		 * Set<Role> roles = new HashSet<Role>();
		 * 
		 * Role role1 = new Role();
		 * role.setId(2);
		 * roles.add(role);
		 * Role role2 = new Role();
		 * role2.setId(4);
		 * roles.add(role2);
		 * 
		 * Role role = new Role();
		 * role.setName("manager");
		 * 
		 * role.setParents(roles);
		 * new RoleManager().addRole(role);
		 */

		/*
		 * Set<Privilege> privs = new HashSet<Privilege>();
		 * Role root = new Role();
		 * root.setId(1);
		 * Privilege priv1 = new Privilege();
		 * priv1.setId(55);
		 * privs.add(priv1);
		 * Privilege priv2 = new Privilege();
		 * priv2.setIdx(56);
		 * privs.add(priv2);
		 * new RoleManager().assignPrivilegeForRole(root,prvis);
		 */
		/*
		 * Set<Privilege> privs = new HashSet<Privilege>();
		 * Role root = new Role();
		 * root.setId(2);
		 * Privilege priv1 = new Privilege();
		 * priv1.setId(57);
		 * privs.add(priv1);
		 * Privilege priv2 = new Privilege();
		 * priv2.setIdx(58);
		 * privs.add(priv2);
		 * new RoleManager().assignPrivilegeForRole(root,prvis);
		 */
		/*
		 * Set<Privilege> privs = new HashSet<Privilege>();
		 * Role root = new Role();
		 * root.setId(4);
		 * Privilege priv1 = new Privilege();
		 * priv1.setId(59);
		 * privs.add(priv1);
		 * new RoleManager().assignPrivilegeForRole(root,prvis);
		 */
	}
}
