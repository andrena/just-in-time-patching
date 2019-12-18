package net.amygdalum.testrecorder.remoteagent;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

public class RemoteAgent {

	public static volatile RemoteAgent agent;

	private Instrumentation instrumentation;
	public ResettableClassFileTransformer runningTransformer;

	public RemoteAgent(Instrumentation instrumentation) {
		this.instrumentation = instrumentation;
		Transformer transformer = (builder, type, loader, module) -> builder
			.visit(Advice.to(Template.class).on(ElementMatchers.hasMethodName("add")));
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
			agent = new RemoteAgent(instrumentation);
		}
	}
}
