package net.amygdalum.testrecorder.remoteagent;

import net.bytebuddy.asm.Advice;

public class Template {

	@Advice.OnMethodExit
	static void exit(@Advice.This Object self) {
		System.out.println("again exit1");
	}

}