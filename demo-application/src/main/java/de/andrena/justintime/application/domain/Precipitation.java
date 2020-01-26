package de.andrena.justintime.application.domain;

public enum Precipitation {
	DRY("trocken"), MIST("Nebel"), DRIZZLE("Nieselregen"), RAIN("Regen"), SNOW("Schnee");

	private String label;

	Precipitation(String label) {
		this.label = label;
	}

	public Precipitation worse() {
		switch (this) {
		case DRY:
			return MIST;
		case MIST:
			return DRIZZLE;
		default:
			return RAIN;
		}
	}

	public String getLabel() {
		return label;
	}
}
