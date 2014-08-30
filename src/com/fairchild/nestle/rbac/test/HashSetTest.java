/*
 * Created on May 17, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.nestle.rbac.test;

import java.util.*;

public class HashSetTest {
	public static void main(String[] args) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("String", "String");
		System.out.println(map.get(null));
		System.out.println(map.containsKey(null));
	}
}
