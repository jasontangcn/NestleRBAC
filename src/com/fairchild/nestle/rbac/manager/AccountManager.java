/*
 * Created on May 7, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.nestle.rbac.manager;

import java.util.*;

import org.hibernate.criterion.*;
import org.hibernate.*;

import com.fairchild.nestle.rbac.Account;
import com.fairchild.nestle.rbac.LoginException;
import com.fairchild.nestle.rbac.Privilege;
import com.fairchild.nestle.rbac.Role;
import com.fairchild.nestle.rbac.util.SessionFactoryHelper;

public class AccountManager {
	public void addAccount(String name, String pwd, int[] roleIds) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = SessionFactoryHelper.currentSession();
			tx = session.beginTransaction();

			Account account = new Account();
			account.setName(name);
			account.setPwd(pwd);

			HashSet<Role> roles = new HashSet<Role>();
			;
			if ((null != roleIds) && (roleIds.length > 0)) {
				for (int id : roleIds) {
					Role role = (Role) session.load(Role.class, id);
					roles.add(role);
				}
				account.setRoles(roles);
			}
			session.persist(account);

			tx.commit();
			session.flush();
		} catch (Exception e) {
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

	public Account login(String name, String pwd) throws LoginException {
		Session session = null;
		Transaction tx = null;
		boolean flag = false;
		Account accountLogined = null;
		try {
			session = SessionFactoryHelper.currentSession();
			tx = session.beginTransaction();
			HashMap<String, String> conditions = new HashMap<String, String>();
			conditions.put("name", name);
			conditions.put("pwd", pwd);
			List al = session.createCriteria(Account.class).add(Expression.allEq(conditions)).list();
			if (al.size() == 0) {
				throw new LoginException();
			} else {
				accountLogined = (Account) al.get(0);
			}
			tx.commit();
			session.flush();
			flag = true;
			return accountLogined;
		} finally {
			if (!flag) {
				try {
					if (null != tx)
						tx.rollback();
				} catch (HibernateException e) {

				}
			}
			try {
				if (null != session)
					session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		/*
		Set<Role> roles = new HashSet<Role>();
		Role role = new Role();
		role.setId(5);
		roles.add(role);
		*/
		int[] roleIds = {5};
		new AccountManager().addAccount("jastang", "123456", roleIds);

		Account jastang = new AccountManager().login("jastang", "123456");
		Privilege p = new Privilege();
		p.setId(57);
		System.out.println(jastang.hasPrivilege(p));

	}
}
