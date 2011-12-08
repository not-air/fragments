package com.not_air.sample.java2x;

import java.lang.reflect.Method;

public class MethodListSample {
    private String className;

    MethodListSample(String className) {
	this.className = className;
    }

    protected String getClassName() {
	return this.className;
    }

    public void list() {
	Class<?> c = null;
	try {
	    c = Class.forName(getClassName());
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}

	Method[] ms = c.getDeclaredMethods();
	for (Method m : ms) {
	    Class<?> r = m.getReturnType();
	    System.out.print(r);
	    System.out.print(" ");
	    System.out.print(m.getName());
	    System.out.print("(");

	    boolean isFirst = true;
	    Class<?>[] ps = m.getParameterTypes();
	    for (Class<?> p : ps) {
		if (!isFirst) {
		    System.out.print(", ");
		}
		System.out.print(p.getName());
		isFirst = false;
	    }
	    System.out.println(")");
	}
    }

    public static void main(String[] argv) {
	if (argv.length == 0) {
	    System.err.println("No inputs.");
	    return;
	}

	MethodListSample m = new MethodListSample(argv[0]);
	m.list();
    }
}
