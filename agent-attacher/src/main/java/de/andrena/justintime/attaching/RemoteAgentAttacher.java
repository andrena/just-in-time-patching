package de.andrena.justintime.attaching;

import java.io.File;
import java.net.URISyntaxException;
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
 * start with java de.andrena.justintime.attaching.RemoteAgentAttacher <agent-jar> <main-class-or-process-id> [<action>]
 * - agent jar is a jar containing a java agent
 * - main class / process ID is the main class / process ID of the java process to attach to
 * - action `attach` a new agent or `detach` an existing one
 */
public class RemoteAgentAttacher {

	public static void main(String[] args) throws Exception {
		try {
			File agentJar = Paths.get(args[0]).toFile();
			String processId = processIdFor(args[1]);
			String action = args.length > 2 ? args[2] : "attach";

			System.out.println("Connecting for " + action + ": " + processId);
			ByteBuddyAgent.attach(agentJar, processId, action);
			System.out.println("Disconnecting from " + action + ": " + processId);
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
		}
	}

	private static String processIdFor(String mainClassOrPID) throws MonitorException, URISyntaxException {
		MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(new HostIdentifier((String) null));
		Set<Integer> activeVmPids = monitoredHost.activeVms();
		for (Integer vmPid : activeVmPids) {
			MonitoredVm mvm = monitoredHost.getMonitoredVm(new VmIdentifier(vmPid.toString()));
			String mvmMainClass = MonitoredVmUtil.mainClass(mvm, true);
			System.out.println("detected VM PID: " + vmPid + ", main class: \"" + mvmMainClass + "\"");
			if (mvmMainClass.contains(mainClassOrPID) || mainClassOrPID.equals("" + vmPid)) {
				return vmPid.toString();
			}
		}

		throw new NoSuchElementException("cannot find any java process with main class or process ID \"" + mainClassOrPID + "\".");
	}
}
