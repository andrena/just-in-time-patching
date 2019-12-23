package de.andrena.justintime.patching;

import de.andrena.justintime.application.domain.DateSource;
import net.bytebuddy.implementation.bind.annotation.Argument;

public class PatchingTemplate {
	
	public static boolean badStellarConfiguration(@Argument(0) DateSource date) {
		int divisor = date.getMonth() % date.getHoursOfDay();
		if (divisor == 0) {
			return false;
		}
		return date.getDayOfMonth() % divisor == 0;  
	}
}