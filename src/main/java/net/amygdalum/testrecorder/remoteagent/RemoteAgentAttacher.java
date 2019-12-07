package net.amygdalum.testrecorder.remoteagent;

import java.io.File;

import net.bytebuddy.agent.ByteBuddyAgent;

public class RemoteAgentAttacher {
	public static void main(String[] args) {
		File agentJar = new File(args[0]);
		String processId = args[1];
		System.out.println("Attaching:" + processId);
		ByteBuddyAgent.attach(agentJar, processId);
		System.out.println("Detaching:" + processId);
	}
}
