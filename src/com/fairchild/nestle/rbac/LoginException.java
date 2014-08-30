/*
 * Created on May 7, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.nestle.rbac;

public class LoginException extends Exception {
	static final long serialVersionUID = -3387516903124229948L;

	public LoginException() {
		super("Username or password is not correct.");
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}

}
