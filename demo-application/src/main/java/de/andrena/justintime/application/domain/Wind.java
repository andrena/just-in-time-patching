package de.andrena.justintime.application.domain;

public enum Wind {
	CALM("still"), BREEZE("mäßig"), WIND("windig"), STORM("stürmisch");

	private String label;

	Wind(String label) {
		this.label = label;
	}

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

	public String label() {
		return label;
	}
}
