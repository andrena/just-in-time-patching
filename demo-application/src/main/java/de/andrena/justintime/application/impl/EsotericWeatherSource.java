package de.andrena.justintime.application.impl;

import de.andrena.justintime.application.domain.DateSource;
import de.andrena.justintime.application.domain.Weather;
import de.andrena.justintime.application.domain.WeatherSource;

public class EsotericWeatherSource implements WeatherSource {

	private WeatherSource source;
	private Esoterics esoterics;

	public EsotericWeatherSource(WeatherSource source) {
		this(source, new Esoterics());
	}

	public EsotericWeatherSource(WeatherSource source, Esoterics esoterics) {
		this.source = source;
		this.esoterics = esoterics;
	}

	@Override
	public Weather getWeather(DateSource date) {
		Weather weather = source.getWeather(date);
		return esoterics.applyEsotericKnowledge(date, weather);
	}
}
