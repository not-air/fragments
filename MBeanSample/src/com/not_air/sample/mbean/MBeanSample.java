package com.not_air.sample.mbean;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import com.sun.jdmk.comm.HtmlAdaptorServer;

public class MBeanSample extends Thread {
    String message;

    public MBeanSample(String message) {
	this.message = message;
    }

    public String getMessage() {
	return message;
    }

    public void updateMessage(String message) {
	this.message = message;
    }

    public void shutdown() {
	synchronized (this) {
	    this.notify();
	}
    }

    public void run() {
	try {
	    synchronized (this) {
		wait();
	    }
	} catch (InterruptedException e) {
	}
    }

    public static void main(String[] argv) {
	String message = null;
	if (argv.length == 0) {
	    message = "Hello!";
	} else {
	    message = argv[0];
	}

	MBeanSample obj = new MBeanSample(message);
	MBeanSampleMBeanImpl impl = new MBeanSampleMBeanImpl();
	impl.attachObject(obj);
	
	StandardMBean mbean = null;
	try {
	    mbean = new StandardMBean(impl, MBeanSampleMBean.class);
	} catch (NotCompliantMBeanException e) {
	    e.printStackTrace();
	    return;
	}

	MBeanServer s = MBeanServerFactory.createMBeanServer();
	HtmlAdaptorServer a = null;
	try {
	    s.registerMBean(mbean, new ObjectName("MBean:name=MBeanSample"));

	    a = new HtmlAdaptorServer();
	    s.registerMBean(a, new ObjectName("Adaptor:name=adaptor,port=8082"));
	} catch (InstanceAlreadyExistsException e) {
	    e.printStackTrace();
	} catch (MBeanRegistrationException e) {
	    e.printStackTrace();
	} catch (NotCompliantMBeanException e) {
	    e.printStackTrace();
	} catch (MalformedObjectNameException e) {
	    e.printStackTrace();
	} catch (NullPointerException e) {
	    e.printStackTrace();
	}

	a.start();
	obj.start();
	try {
	    obj.join();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	a.stop();
    }
}
