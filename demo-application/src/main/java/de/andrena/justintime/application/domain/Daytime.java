package de.andrena.justintime.application.domain;

public enum Daytime {
	NIGHT(0, "Nacht"), MORNING(8, "Morgen"), NOON(12, "Mittag"), AFTERNOON(16, "Nachmittag"), EVENING(20, "Abend");

	private int hours;
	private String label;

	Daytime(int hours, String label) {
		this.hours = hours;
		this.label = label;
	}

	public int hours() {
		return hours;
	}

	public String label() {
		return label;
	}

}
