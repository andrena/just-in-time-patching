package de.andrena.justintime.application.domain;

public enum Precipitation {
	DRY, NORMAL, MIST, DRIZZLE, RAIN, SNOW;

	public Precipitation worse() {
		switch (this) {
		case DRY:
			return NORMAL;
		case NORMAL:
			return MIST;
		case MIST:
			return DRIZZLE;
		default:
			return RAIN;
		}
	}
}
