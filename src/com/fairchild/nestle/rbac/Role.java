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
public class Role {
	private int id;

	private String name;

	private int version;

	private Set<Role> parents;

	private Set<Privilege> privileges;

	private Set<Account> accounts;

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

	@Version
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<Role> getParents() {
		return parents;
	}

	public void setParents(Set<Role> parents) {
		this.parents = parents;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "roles")
	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public int hashCode() {
		if (id != 0)
			return id;
		if (null != name)
			return name.hashCode();
		throw new RBACException("id or name must be set.");
	}

	public boolean equals(Object obj) {
		Role role = null;
		if (obj instanceof Role) {
			role = (Role) obj;
		} else {
			return false;
		}
		if ((id == 0) && (role.getId() == 0)) {
		} else {
			if (id == role.getId()) {
				return true;
			} else {
				return false;
			}
		}

		if ((null == name) && (null == role.getName())) {
			return false;
		} else {
			if (null != name) {
				if (name.equals(role.getName())) {
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
		if ((null != this.privileges) && (this.privileges.contains(p)))
			return true;
		if ((null != parents) && (parents.size() > 0)) {
			for (Role role : parents) {
				return role.hasPrivilege(p);
			}
		}
		return false;
	}
}
