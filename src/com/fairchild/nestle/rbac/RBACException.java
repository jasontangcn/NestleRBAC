/*
 * Created on Jun 18, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.nestle.rbac;

public class RBACException extends RuntimeException {

	public RBACException() {
		super();
	}

	public RBACException(String message) {
		super(message);
	}

	public RBACException(String message, Throwable cause) {
		super(message, cause);
	}

	public RBACException(Throwable cause) {
		super(cause);
	}

}
