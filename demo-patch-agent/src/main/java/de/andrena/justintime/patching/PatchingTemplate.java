package de.andrena.justintime.patching;

import java.util.Arrays;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.This;

public class PatchingTemplate {
	
	public static int method(@This Object self, @AllArguments Object[] objects) {
		System.out.println("skipping add of " + Arrays.toString(objects));
		return 1;
	}
}