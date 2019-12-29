package de.andrena.justintime.patching;

import static net.amygdalum.xrayinterface.XRayInterface.xray;

import de.andrena.justintime.application.domain.DateSource;
import de.andrena.justintime.application.fake.Esoterics;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.This;

public class PatchingTemplate {

	public static boolean badStellarConfiguration(@This Esoterics self, @Argument(0) DateSource date) {
		WithSourceOfKnowledge openSelf = xray(self).to(WithSourceOfKnowledge.class);
		openSelf.setSourceOfKnowledge((openSelf.getSourceOfKnowledge() + 1) % 6);
		int sourceOfKnowledge = openSelf.getSourceOfKnowledge();
		if (sourceOfKnowledge == 0) {
			return false;
		}
		return date.getDayOfMonth() % sourceOfKnowledge == 0;
	}

	public interface WithSourceOfKnowledge {
		int getSourceOfKnowledge();

		void setSourceOfKnowledge(int sourceOfKnowledge);
	}
}