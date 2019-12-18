package net.amygdalum.testrecorder.remoteagent;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Set;

import net.bytebuddy.agent.ByteBuddyAgent;
import sun.jvmstat.monitor.HostIdentifier;
import sun.jvmstat.monitor.MonitorException;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

/**
 * start with java net.amygdalum.testrecorder.remoteagent.RemoteAgentAttacher <agent-jar> <main-class> [<action>]
 * - agent jar is a jar containing a java agent
 * - main class is the main class of the java process to attach to (a list is printed)
 * - action `attach` a new agent or `detach` an existing one 
 */
@SuppressWarnings("restriction")
public class RemoteAgentAttacher {
	public static void main(String[] args) throws Exception {
		Path agentJarPath = Paths.get(args[0]);
		File agentJar = agentJarPath.toFile();

		String mainClass = args[1];
		String action = args.length > 2 ? args[2] : "attach";
		try {
			String processId = processIdFor(mainClass);
			System.out.println("Connecting for " + action + ":" + processId);
			ByteBuddyAgent.attach(agentJar, processId, action);
			System.out.println("Disconnecting from " + action + ":" + processId);
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage() + " Consider one of the list above.");
		}

	}

	public static String processIdFor(String name) throws MonitorException, URISyntaxException {
		MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(new HostIdentifier((String) null));
		Set<Integer> activeVmPids = monitoredHost.activeVms();
		for (Integer vmPid : activeVmPids) {
			MonitoredVm mvm = monitoredHost.getMonitoredVm(new VmIdentifier(vmPid.toString()));
			String mvmMainClass = MonitoredVmUtil.mainClass(mvm, true);
			System.out.println(vmPid + ":" + mvmMainClass);
			if (mvmMainClass.contains(name)) {
				return vmPid.toString();
			}
		}
		throw new NoSuchElementException("cannot find " + name + " in java process main classes.");

	}
}
