package de.andrena.justintime.patching;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class PatchAgent {

	public static volatile PatchAgent agent;

	private Instrumentation instrumentation;
	private ResettableClassFileTransformer runningTransformer;

	public PatchAgent(Instrumentation instrumentation) {
		this.instrumentation = instrumentation;
		Transformer transformer = (builder, type, loader, module) -> builder
			.method(ElementMatchers.hasMethodName("add"))
			.intercept(MethodDelegation.to(PatchingTemplate.class));
		runningTransformer = new AgentBuilder.Default()
			.ignore(ElementMatchers.none())
			.disableClassFormatChanges()
			.with(new AgentBuilder.CircularityLock.Default())
			.with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
			.with(new AgentBuilder.Listener.WithErrorsOnly(AgentBuilder.Listener.StreamWriting.toSystemError()))
			.type(ElementMatchers.nameContains("TodoList"))
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
		if (arg.equals("detach")) {
			System.out.println("detaching");
			if (agent != null) {
				System.out.println("agent to detach exists");
				agent.shutdown();
				agent = null;
			} else {
				System.out.println("no agent to detach exists");
			}
		} else {
			System.out.println("attaching");
			agent = new PatchAgent(instrumentation);
		}
	}
}
