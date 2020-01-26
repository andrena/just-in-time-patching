package de.andrena.justintime.application.domain;

public enum Temperature {
	HOT("heiß"), WARM("warm"), MODERATE("moderat"), COOL("kühl"), COLD("kalt"), FREEZING("eiskalt");

	private String label;

	Temperature(String label) {
		this.label = label;
	}

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

	public String getLabel() {
		return label;
	}
}
