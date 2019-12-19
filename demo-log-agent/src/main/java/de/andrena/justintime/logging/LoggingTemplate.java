package de.andrena.justintime.logging;

import net.bytebuddy.asm.Advice;

public class LoggingTemplate {

	@Advice.OnMethodExit
	static void exit(@Advice.This Object self) {
		System.out.println("exit");
	}

}