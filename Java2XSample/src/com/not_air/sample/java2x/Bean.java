package com.not_air.sample.java2x;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class Bean {
    String name;
    List<Value> fields = new ArrayList<Value>();

    Bean() {
    }

    void init(CtClass c) {
	name = c.getName();
	Map<String, CtMethod> getter = new LinkedHashMap<String, CtMethod>();
	Map<String, CtMethod> setter = new LinkedHashMap<String, CtMethod>();
	CtMethod[] ms = c.getMethods();
	for (CtMethod m : ms) {
	    String name = m.getName();
	    if (name.startsWith("get")) {
		getter.put(getFieldName(name), m);
	    } else if (name.startsWith("set")) {
		setter.put(getFieldName(name), m);
	    }
	}

	for (Map.Entry<String, CtMethod> m : getter.entrySet()) {
	    String name = m.getKey();
	    CtMethod s = setter.get(name);
	    if (s == null) {
		continue;
	    }

	    try {
		CtClass[] args = s.getParameterTypes();
		if (args.length != 1) {
		    continue;
		}

		CtMethod g = m.getValue();
		CtClass r = g.getReturnType();
		if (r.equals(args[0])) {
		    fields.add(new Value(r.getName(), name));
		}
	    } catch (NotFoundException e) {
		continue;
	    }
	}
    }

    private static String getFieldName(String method) {
	if (method.startsWith("get") || method.startsWith("set")) {
	    return Character.toLowerCase(method.charAt(3))
		    + method.substring(4);
	} else {
	    return method;
	}
    }

    public String getName() {
	return name;
    }

    public List<Value> getFields() {
	return fields;
    }

    static public Bean createBean(String name) {
	ClassPool pool = ClassPool.getDefault();
	CtClass c = null;
	try {
	    c = pool.get(name);
	} catch (NotFoundException e) {
	    e.printStackTrace();
	    return null;
	}
		
	return createBean(c);
    }

    static public Bean createBean(CtClass c) {
	try {
	    if (c.isArray()) {
		c = c.getComponentType();
	    } else if (c.isPrimitive()
		    || c.getName().equals(String.class.getName())) {
		// do nothing.
		return null;
	    }

	    Bean b = new Bean();
	    b.init(c);
	    return b;
	} catch (NotFoundException e) {
	    return null;
	}
    }

}