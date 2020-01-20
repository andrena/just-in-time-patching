package de.andrena.justintime.patching;

import de.andrena.justintime.application.domain.DateSource;
import de.andrena.justintime.application.impl.Esoterics;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.This;

public class PatchingTemplate {

	public static boolean badStellarConfiguration(@This Esoterics self, @Argument(0) DateSource date) {
		self.updateSourceOfKnowledge();
		if (self.sourceOfKnowledge() == 0) {
			return false;
		}
		return date.getHoursOfDay() % self.sourceOfKnowledge() == 0;
	}
}