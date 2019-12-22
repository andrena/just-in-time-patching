package de.andrena.justintime.application.domain;

public enum Daytime {
	NIGHT(0), MORNING(8), NOON(12), AFTERNOON(16), EVENING(20);

	private int hours;

	Daytime(int hours) {
		this.hours = hours;
	}
	
	public int hours() {
		return hours;
	}

}
