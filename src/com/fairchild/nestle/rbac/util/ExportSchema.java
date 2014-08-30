/*
 * Created on May 7, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.nestle.rbac.util;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;

import com.fairchild.nestle.rbac.Account;
import com.fairchild.nestle.rbac.Privilege;
import com.fairchild.nestle.rbac.Role;

public class ExportSchema {
	public static SessionFactory sf = null;
	static {
		AnnotationConfiguration config = new AnnotationConfiguration();
		Class[] clazz = {Account.class, Role.class, Privilege.class };
		List<Class> al = Arrays.asList(clazz);
		config.addAnnotatedClasses(al);
		config.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
		sf = config.buildSessionFactory();
	}

	public static Session openSession() {
		return sf.openSession();
	}

	public static void main(String[] args) {
		openSession().close();
	}
}
