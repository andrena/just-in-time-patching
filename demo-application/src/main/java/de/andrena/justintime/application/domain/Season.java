package de.andrena.justintime.application.domain;

public enum Season {
	SPRING("Frühling"), SUMMER("Sommer"), FALL("Herbst"), WINTER("Winter");

	private String label;

	Season(String label) {
		this.label = label;
	}

	public String label() {
		return label;
	}
}
