package de.andrena.justintime.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import de.andrena.justintime.application.Server;
import de.andrena.justintime.application.domain.CalendarDateSource;
import de.andrena.justintime.application.domain.WeatherSource;
import io.vertx.ext.web.RoutingContext;
import net.bytebuddy.asm.Advice;

public class LoggingTemplate {

	@Advice.OnMethodEnter
	public static void exit(@Advice.This Server self, @Advice.Argument(0) RoutingContext context, @Advice.FieldValue("weather") WeatherSource weather) {
		try {
			int year = Integer.parseInt(context.request().getParam("year"));
			int month = Integer.parseInt(context.request().getParam("month"));
			int day = Integer.parseInt(context.request().getParam("day"));
			int hours = Integer.parseInt(context.request().getParam("hours"));

			CalendarDateSource time = new CalendarDateSource(year, month, day, hours);
			System.out.println("date:" + formatDate(time.getDate()));
			System.out.println("hours:" + time.getHoursOfDay());
			System.out.println("season:" + time.getSeason());
			System.out.println("weekday:" + time.getWeekday());
			System.out.println("daytime:" + time.getDaytime());
			System.out.println("weather:" + weather.getWeather(time));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String formatDate(LocalDateTime date) {
		return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(date);
	}

}