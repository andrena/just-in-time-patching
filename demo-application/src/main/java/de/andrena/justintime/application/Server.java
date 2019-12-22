package de.andrena.justintime.application;

import de.andrena.justintime.application.domain.CalendarDateSource;
import de.andrena.justintime.application.domain.WeatherSource;
import de.andrena.justintime.application.fake.EsotericWeatherSource;
import de.andrena.justintime.application.fake.SimulatedWeatherSource;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine;

public class Server extends AbstractVerticle {

	private TemplateEngine engine;
	private Router router;

	private WeatherSource weather;


	public Server() {
		weather = new EsotericWeatherSource(new SimulatedWeatherSource());
	}

	public void start() {
		this.engine = HandlebarsTemplateEngine.create(vertx);
		this.router = Router.router(vertx);
		router.route("/day/:year/:month/:day/:hours").handler(this::predict);
		router.route().handler(this::show);

		HttpServer server = vertx.createHttpServer();
		server.requestHandler(router).listen(8080);
	}

	public void show(RoutingContext context) {
		CalendarDateSource time = new CalendarDateSource();
		context.data().put("date", time.getDate());
		context.data().put("hours", time.getHours());
		context.data().put("season", time.getSeason());
		context.data().put("weekday", time.getWeekday());
		context.data().put("daytime", time.getDaytime());
		context.data().put("weather", weather.getWeather(time));
	}

	public void predict(RoutingContext context) {
		int year = Integer.parseInt(context.request().getParam("year"));
		int month = Integer.parseInt(context.request().getParam("month"));
		int day = Integer.parseInt(context.request().getParam("day"));
		int hours = Integer.parseInt(context.request().getParam("hours"));

		CalendarDateSource time = new CalendarDateSource(year, month, day, hours);
		context.data().put("date", time.getDate());
		context.data().put("hours", time.getHours());
		context.data().put("season", time.getSeason());
		context.data().put("weekday", time.getWeekday());
		context.data().put("daytime", time.getDaytime());
		context.data().put("weather", weather.getWeather(time));

		engine.render(context.data(), "src/main/resources/index.html", res -> {
			if (res.succeeded()) {
				context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
			} else {
				context.fail(res.cause());
			}
		});
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Server());
	}
}
