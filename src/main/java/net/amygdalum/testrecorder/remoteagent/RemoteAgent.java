package net.amygdalum.testrecorder.remoteagent;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

public class RemoteAgent {
	public static void premain(String arg, Instrumentation instrumentation) {
		agentmain(arg, instrumentation);
	}
	
	public static void agentmain(String arg, Instrumentation instrumentation) {
		Transformer transformer = (builder, type, loader, module) -> builder
			.visit(Advice.to(Template.class).on(ElementMatchers.hasMethodName("add")));
		new AgentBuilder.Default()
			.ignore(ElementMatchers.none())
			.disableClassFormatChanges()
			.with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
			.type(ElementMatchers.nameContains("TodoList"))
			.transform(transformer)
			.installOn(instrumentation);
	}

	static class Template {

		@Advice.OnMethodExit
		static void exit(@Advice.This Object self) {
			System.out.println("exit");
		}

	}
}
