/*
 * Created on 2004-10-26
 *
 */
package com.fairchild.nestle.rbac;

/**
 * @author TomHornson@hotmail.com
 *
 */
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.*;

@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true, selectBeforeUpdate = true)
@javax.persistence.Entity(access = AccessType.PROPERTY)
@BatchSize(size = 50)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Account {
	private int id;
	private String name;
	private String pwd;
	private int version;
	private Set<Role> roles;

	@Id(generate = GeneratorType.AUTO)
	@Column(updatable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(nullable = false, updatable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false)
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Version
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public int hashCode() {
		if (id != 0)
			return id;
		if (null != name)
			return name.hashCode();
		throw new RBACException("id or name must be set.");
	}

	public boolean equals(Object obj) {
		Account account = null;
		if (obj instanceof Account) {
			account = (Account) obj;
		} else {
			return false;
		}
		if ((id == 0) && (account.getId() == 0)) {
		} else {
			if (id == account.getId()) {
				return true;
			} else {
				return false;
			}
		}

		if ((null == name) && (null == account.getName())) {
			return false;
		} else {
			if (null != name) {
				if (name.equals(account.getName())) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	public String toString() {
		return this.name;
	}

	public boolean hasPrivilege(Privilege p) {
		boolean flag = false;
		if ((null != roles) && (roles.size() > 0)) {
			for (Role role : roles) {
				flag = role.hasPrivilege(p);
				if (flag) break;
			}
		}
		return flag;
	}
}