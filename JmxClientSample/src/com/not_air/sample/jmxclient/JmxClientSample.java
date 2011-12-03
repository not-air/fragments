package com.not_air.sample.jmxclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;

@SuppressWarnings("restriction")
public class JmxClientSample {
	public static void main(String[] argv) {
		System.out.println("ApplicationProviders:");
		VirtualMachineDescriptor target = null;
		List<AttachProvider> aps = AttachProvider.providers();
		for (AttachProvider ap : aps) {
			System.out.println("- name: " + ap.name());
			System.out.println("  type: " + ap.type());
			System.out.println("  VirtualMachineDescriptors:");

			List<VirtualMachineDescriptor> vmds = ap.listVirtualMachines();
			for (VirtualMachineDescriptor vmd : vmds) {
				System.out.println("  - name: " + vmd.displayName());
				System.out.println("    id: " + vmd.id());

				if ((argv[0] != null && vmd.displayName() != null)
						&& vmd.displayName().indexOf(argv[0]) != -1) {
					target = vmd;
				}
			}
		}

		String connectorAddress = null;
		try {
			VirtualMachine vm = VirtualMachine.attach(target);
			connectorAddress = vm.getAgentProperties().getProperty(
					"com.sun.management.jmxremote.localConnectorAddress");
		} catch (AttachNotSupportedException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		JMXServiceURL url = null;
		try {
			// url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi", "localhost", 9012, "/jmxrmi");
			// url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9012/jmxrmi");
			url = new JMXServiceURL(connectorAddress);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		}

		try {
			System.out.println("JMXconnection:");
			System.out.println("  url: " + url);
			System.out.println("  Objects:");
			JMXConnector connector = JMXConnectorFactory.connect(url);
			MBeanServerConnection con = connector.getMBeanServerConnection();
			Set<ObjectInstance> s = con.queryMBeans(null, null);
			for (ObjectInstance o : s) {
				System.out.println("  - name: " + o.getClassName());

				ObjectName n = o.getObjectName();
				System.out.println("    canonicalproperyt: "
						+ n.getCanonicalKeyPropertyListString());
				System.out.println("    canonicalname: " + n.getCanonicalName());
				System.out.println("    domain: " + n.getDomain());
				System.out.println("    properties:");

				Hashtable<String, String> t = n.getKeyPropertyList();
				for (Map.Entry<String, String> entry : t.entrySet()) {
					System.out.println("      " + entry.getKey() + ":"
							+ entry.getValue());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
