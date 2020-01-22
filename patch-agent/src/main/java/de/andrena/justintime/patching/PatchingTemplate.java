package de.andrena.justintime.patching;

import de.andrena.justintime.application.domain.DateSource;
import de.andrena.justintime.application.esoterics.Esoterics;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.This;

public class PatchingTemplate {

	public static boolean badStellarConstellation(@This Esoterics self, @Argument(0) DateSource date) {
		self.updateSourceOfKnowledge();
		int sourceOfKnowledge = self.sourceOfKnowledge();
		if (sourceOfKnowledge == 0) {
			System.out.println("fixed: stellar constellation is fine");
			return false;
		}
		return date.getHoursOfDay() % sourceOfKnowledge == 0;
	}
}