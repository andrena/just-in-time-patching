package de.andrena.justintime.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import de.andrena.justintime.application.WeatherServer;
import de.andrena.justintime.application.domain.LocalDateTimeSource;
import io.vertx.ext.web.RoutingContext;
import net.bytebuddy.asm.Advice;

public class LoggingTemplate {

	@Advice.OnMethodEnter
	public static void predict(@Advice.This WeatherServer self, @Advice.Argument(0) RoutingContext context) {
		int year = Integer.parseInt(context.request().getParam("year"));
		int month = Integer.parseInt(context.request().getParam("month"));
		int day = Integer.parseInt(context.request().getParam("day"));
		int hours = Integer.parseInt(context.request().getParam("hours"));

		LocalDateTimeSource time = new LocalDateTimeSource(year, month, day, hours);
		System.out.println();
		System.out.println("entering predict method with:");
		System.out.println("date:" + formatDate(time.getDate()));
		System.out.println("hours:" + time.getHoursOfDay());
		System.out.println("season:" + time.getSeason());
		System.out.println("weekday:" + time.getWeekday());
		System.out.println("daytime:" + time.getDaytime());
	}

	@Advice.OnMethodExit(onThrowable = Throwable.class)
	public static void predict(@Advice.This WeatherServer self, @Advice.Thrown Throwable thrown) {
		if (thrown != null) {
			System.out.println();
			System.out.println("predict method threw exception:");
			thrown.printStackTrace();
		}
	}

	public static String formatDate(LocalDateTime date) {
		return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(date);
	}

}