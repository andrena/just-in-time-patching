package de.andrena.justintime.application.domain;

public class Weather {

	private Precipitation precipitation;
	private Temperature temperature;
	private Wind wind;
	
	public Weather(Precipitation precipitation, Temperature temperature, Wind wind) {
		this.precipitation = precipitation;
		this.temperature = temperature;
		this.wind = wind;
	}

	@Override
	public String toString() {
		return temperature + "/" + getPrecipitation() + "/" + wind;
	}

	public Precipitation getPrecipitation() {
		return precipitation;
	}
	
	public Temperature getTemperature() {
		return temperature;
	}
	
	public Wind getWind() {
		return wind;
	}
	
	
}
