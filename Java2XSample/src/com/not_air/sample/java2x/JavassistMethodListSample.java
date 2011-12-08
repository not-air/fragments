package com.not_air.sample.java2x;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

public class JavassistMethodListSample extends MethodListSample {
    protected String className;

    JavassistMethodListSample(String className) {
	super(className);
    }

    @Override
    public void list() {
	ClassPool pool = ClassPool.getDefault();
	CtClass c = null;
	try {
	    c = pool.get(getClassName());
	} catch (NotFoundException e) {
	    e.printStackTrace();
	    return;
	}

	CtMethod[] ms = c.getDeclaredMethods();
	for (CtMethod m : ms) {
	    CodeAttribute ca = m.getMethodInfo().getCodeAttribute();
	    AttributeInfo a = ca.getAttribute("LocalVariableTable");
	    if (!(a instanceof LocalVariableAttribute)) {
		return;
	    }

	    CtClass r = null;
	    try {
		r = m.getReturnType();
	    } catch (NotFoundException e) {
		e.printStackTrace();
		continue;
	    }

	    System.out.print(r.getName());
	    System.out.print(" ");
	    System.out.print(m.getName());
	    System.out.print("(");

	    LocalVariableAttribute lva = (LocalVariableAttribute) a;
	    CtClass[] ps = null;
	    try {
		ps = m.getParameterTypes();
	    } catch (NotFoundException e) {
		System.out.println(")");
		e.printStackTrace();
		continue;
	    }

	    // instance method receive "this" object as first argument.
	    if (!isStatic(m)) {
		CtClass[] newPs = new CtClass[ps.length + 1];
		System.arraycopy(ps, 0, newPs, 1, ps.length);
		ps = newPs;
	    }

	    boolean isFirst = true;
	    int i = 0;
	    for (CtClass p : ps) {
		if (!isFirst) {
		    System.out.print(", ");
		}

		String name = lva.getConstPool()
			.getUtf8Info(lva.nameIndex(i++));
		if ("this".equals(name)) {
		    continue;
		}

		System.out.print(p.getName());
		System.out.print(" ");
		System.out.print(name);
		isFirst = false;
	    }
	    System.out.println(")");
	}
    }

    private boolean isStatic(CtMethod m) {
	return Modifier.isStatic(m.getModifiers());
    }

    public static void main(String[] argv) {
	if (argv.length == 0) {
	    System.err.println("No inputs.");
	    return;
	}

	System.out.println("- Reflection");
	MethodListSample m = new MethodListSample(argv[0]);
	m.list();

	System.out.println("");

	System.out.println("- Javassist");
	m = new JavassistMethodListSample(argv[0]);
	m.list();
    }
}
