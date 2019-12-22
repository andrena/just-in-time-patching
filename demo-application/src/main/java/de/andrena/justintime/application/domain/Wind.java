package de.andrena.justintime.application.domain;

public enum Wind {
	CALM, BREEZE, WIND, STORM;

	public Wind worse() {
		switch (this) {
		case CALM:
			return CALM;
		case BREEZE:
			return WIND;
		default:
			return STORM;
		}
	}
}
