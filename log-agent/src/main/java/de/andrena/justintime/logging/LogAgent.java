package de.andrena.justintime.logging;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

public class LogAgent {

	public static volatile List<LogAgent> agents = new ArrayList<>();

	private Instrumentation instrumentation;
	private ResettableClassFileTransformer runningTransformer;

	public LogAgent(Instrumentation instrumentation) {
		this.instrumentation = instrumentation;
		Transformer transformer = (builder, type, loader, module) -> builder
			.visit(Advice.to(LoggingTemplate.class)
				.on(ElementMatchers.hasMethodName("predict")));
		runningTransformer = new AgentBuilder.Default()
			.disableClassFormatChanges()
			.with(new AgentBuilder.CircularityLock.Default())
			.with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
			.with(new AgentBuilder.Listener.WithErrorsOnly(AgentBuilder.Listener.StreamWriting.toSystemError()))
			.type(ElementMatchers.nameContains("WeatherServer"))
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
			LogAgent agent = new LogAgent(instrumentation);
			System.out.println("attaching LogAgent " + System.identityHashCode(agent));
			agents.add(agent);
		} else if (arg.equals("detach")) {
			if (agents.isEmpty()) {
				System.out.println("there is no LogAgent to detach");
			}
			for (LogAgent agent : agents) {
				System.out.println("detaching LogAgent " + System.identityHashCode(agent));
				agent.shutdown();
			}
			agents.clear();
		} else {
			System.out.println("Invalid argument for attaching/detaching LogAgent");
		}
	}
}
