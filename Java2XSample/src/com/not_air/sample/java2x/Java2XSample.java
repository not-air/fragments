package com.not_air.sample.java2x;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class Java2XSample {
    String name;
    String simpleName;
    String packageName;
    List<Method> methods = new ArrayList<Method>();
    Map<String, Bean> beanMap = new HashMap<String, Bean>();

    public Java2XSample(String name) {
	this.name = name;
    }

    public void compile() {
	ClassPool pool = ClassPool.getDefault();
	CtClass c = null;
	try {
	    c = pool.get(name);
	} catch (NotFoundException e) {
	    e.printStackTrace();
	    return;
	}

	simpleName = c.getSimpleName();
	packageName = c.getPackageName();

	CtMethod[] ms = c.getDeclaredMethods();
	for (CtMethod m : ms) {
	    Method method = new Method();
	    List<Bean> beans = method.init(m);
	    for (Bean b : beans) {
		addBean(b);
	    }

	    methods.add(method);
	}
    }

    public void generate(OutputStream os) {
	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
	try {
	    writer.write(packageName);
	    writer.newLine();
	    writer.write(simpleName);
	    writer.newLine();
	    writer.newLine();

	    for (Method m : methods) {
		writer.write(m.getReturnType());
		writer.write(" ");
		writer.write(m.getName());
		writer.write("(");
		boolean isFirst = true;
		for (Value a : m.getArguments()) {
		    if (!isFirst) {
			writer.write(", ");
		    }
		    writer.write(a.getType());
		    writer.write(" ");
		    writer.write(a.getName());
		    isFirst = false;
		}
		writer.write(")");
		writer.newLine();
	    }

	    writer.newLine();

	    Set<Map.Entry<String, Bean>> s = beanMap.entrySet();
	    for (Map.Entry<String, Bean> entry : s) {
		writer.write(entry.getKey());
		writer.write(" : ");
		writer.newLine();

		List<Value> l = entry.getValue().getFields();
		for (Value v : l) {
		    writer.write("  ");
		    writer.write(v.getType());
		    writer.write(" ");
		    writer.write(v.getName());
		    ;
		    writer.newLine();
		}
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		writer.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    private void addBean(Bean b) {
	beanMap.put(b.getName(), b);
    }

    public static void main(String[] argv) {
	if (argv.length == 0) {
	    System.err.println("No inputs.");
	    return;
	}

	Java2XSample j = new Java2XSample(argv[0]);
	j.compile();
	j.generate(System.out);
    }
}
