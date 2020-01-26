package de.andrena.justintime.patching;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class PatchAgent {

	public static volatile List<PatchAgent> agents = new ArrayList<>();

	private Instrumentation instrumentation;
	private ResettableClassFileTransformer runningTransformer;

	public PatchAgent(Instrumentation instrumentation) {
		this.instrumentation = instrumentation;
		Transformer transformer = (builder, type, loader, module) -> builder
			.method(ElementMatchers.hasMethodName("badStellarConstellation"))
			.intercept(MethodDelegation.to(PatchingTemplate.class));
		runningTransformer = new AgentBuilder.Default()
			.disableClassFormatChanges()
			.with(new AgentBuilder.CircularityLock.Default())
			.with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
			.with(new AgentBuilder.Listener.WithErrorsOnly(AgentBuilder.Listener.StreamWriting.toSystemError()))
			.type(ElementMatchers.nameContains("Esoterics"))
			.transform(transformer)
			.installOn(instrumentation);
	}

	private void shutdown() {
		runningTransformer.reset(instrumentation, AgentBuilder.RedefinitionStrategy.RETRANSFORMATION);
	}

	public static void premain(String arg, Instrumentation instrumentation) {
		agentmain(arg, instrumentation);
	}

	public static void agentmain(String arg, Instrumentation instrumentation) {
		if (arg.equals("attach")) {
			PatchAgent agent = new PatchAgent(instrumentation);
			System.out.println("attaching PatchAgent " + System.identityHashCode(agent));
			agents.add(agent);
		} else if (arg.equals("detach")) {
			if (agents.isEmpty()) {
				System.out.println("there is no PatchAgent to detach");
			}
			for (PatchAgent agent : agents) {
				System.out.println("detaching PatchAgent " + System.identityHashCode(agent));
				agent.shutdown();
			}
			agents.clear();
		} else {
			System.out.println("Invalid argument for attaching/detaching PatchAgent");
		}
	}
}
