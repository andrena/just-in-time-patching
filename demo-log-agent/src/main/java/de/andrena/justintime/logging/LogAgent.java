package de.andrena.justintime.logging;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

public class LogAgent {

	public static volatile LogAgent agent;

	private Instrumentation instrumentation;
	private ResettableClassFileTransformer runningTransformer;

	public LogAgent(Instrumentation instrumentation) {
		this.instrumentation = instrumentation;
		Transformer transformer = (builder, type, loader, module) -> builder
			.visit(Advice.to(LoggingTemplate.class).on(ElementMatchers.hasMethodName("predict")));
		runningTransformer = new AgentBuilder.Default()
			.ignore(ElementMatchers.none())
			.disableClassFormatChanges()
			.with(new AgentBuilder.CircularityLock.Default())
			.with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
			.with(new AgentBuilder.Listener.WithErrorsOnly(AgentBuilder.Listener.StreamWriting.toSystemError()))
			.type(ElementMatchers.nameContains("Server"))
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
			agent = new LogAgent(instrumentation);
		}
	}
}
