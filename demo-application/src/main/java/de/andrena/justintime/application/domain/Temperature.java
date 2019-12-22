package de.andrena.justintime.application.domain;

public enum Temperature {
	HOT, WARM, MODERATE, COOL, COLD, FREEZING;

	public Temperature worse() {
		switch (this) {
		case HOT:
			return HOT;
		case WARM:
			return HOT;
		case MODERATE:
			return COOL;
		case COOL:
			return COLD;
		default:
			return FREEZING;
		}
	}
}
