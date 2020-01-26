package de.andrena.justintime.application.domain;

public enum Weekday {
	SUNDAY("Sonntag"), MONDAY("Montag"), TUESDAY("Dienstag"), WEDNESDAY("Mittwoch"), THURSDAY("Donnerstag"), FRIDAY("Freitag"), SATURDAY("Samstag");

	private String label;

	Weekday(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
