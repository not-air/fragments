package com.not_air.sample.java2x;

import java.util.ArrayList;
import java.util.List;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

public class Method {
    String name;
    List<Value> l = new ArrayList<Value>();
    String returnType;

    Method() {
    }

    List<Bean> init(CtMethod m) {
	List<Bean> beans = new ArrayList<Bean>();
	this.name = m.getName();

	CodeAttribute ca = m.getMethodInfo().getCodeAttribute();
	AttributeInfo a = ca.getAttribute("LocalVariableTable");
	if (!(a instanceof LocalVariableAttribute)) {
	    // TODO : should throw exception
	    return null;
	}

	CtClass r = null;
	try {
	    r = m.getReturnType();
	    addBean(beans, r);
	} catch (NotFoundException e) {
	    e.printStackTrace();
	    return null;
	}
	this.returnType = r.getName();

	LocalVariableAttribute lva = (LocalVariableAttribute) a;
	CtClass[] ps = null;
	try {
	    ps = m.getParameterTypes();
	} catch (NotFoundException e) {
	    e.printStackTrace();
	    return null;
	}

	// instance method receive "this" object as first argument.
	if (!isStatic(m)) {
	    CtClass[] newPs = new CtClass[ps.length + 1];
	    System.arraycopy(ps, 0, newPs, 1, ps.length);
	    ps = newPs;
	}

	for (int i = 0; i < ps.length; i++) {
	    String name = lva.getConstPool().getUtf8Info(lva.nameIndex(i));
	    if ("this".equals(name)) {
		continue;
	    }

	    CtClass p = ps[i];
	    addArgument(p.getName(), name);
	    addBean(beans, p);
	}

	return beans;
    }

    void addArgument(String type, String name) {
	l.add(new Value(type, name));
    }

    public String getName() {
	return name;
    }

    public String getReturnType() {
	return returnType;
    }

    public Value[] getArguments() {
	return l.toArray(new Value[l.size()]);
    }

    static private void addBean(List<Bean> l, CtClass c) {
	Bean b = Bean.createBean(c);
	if (b == null) {
	    return;
	}

	l.add(b);
	List<Value> vs = b.getFields();
	for (Value v : vs) {
	    b = Bean.createBean(v.getType());
	    if (b != null) {
		l.add(b);
	    }
	}
    }

    static private boolean isStatic(CtMethod m) {
	return javassist.Modifier.isStatic(m.getModifiers());
    }
}