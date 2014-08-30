/*
 * Created on 2004-10-26
 *
 */
package com.fairchild.nestle.rbac;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.*;

/**
 * @author TomHornson@hotmail.com
 */
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true, selectBeforeUpdate = true)
@javax.persistence.Entity(access = AccessType.PROPERTY)
@BatchSize(size = 50)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Privilege {
	private int id;
	private String name;
	private String alias;
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

	@Column(unique = true, nullable = false, updatable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(unique = true, nullable = false, updatable = false)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Version
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "privileges")
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
		Privilege p = null;
		if (obj instanceof Privilege) {
			p = (Privilege) obj;
		} else {
			return false;
		}
		if ((id == 0) && (p.getId() == 0)) {
		} else {
			if (id == p.getId()) {
				return true;
			} else {
				return false;
			}
		}

		if ((null == name) && (null == p.getName())) {
			return false;
		} else {
			if (null != name) {
				if (name.equals(p.getName())) {
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
}
