package de.andrena.justintime.recording;

import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import de.andrena.justintime.application.fake.SimulatedWeatherSource;
import net.amygdalum.testrecorder.deserializers.CustomAnnotation;
import net.amygdalum.testrecorder.generator.JUnit5TestTemplate;
import net.amygdalum.testrecorder.generator.TestGeneratorProfile;
import net.amygdalum.testrecorder.generator.TestTemplate;
import net.amygdalum.testrecorder.hints.SkipChecks;

public class MyTestGeneratorProfile implements TestGeneratorProfile {

	@Override
	public List<CustomAnnotation> annotations() {
		try {
			return asList(
				new CustomAnnotation(SimulatedWeatherSource.class.getDeclaredField("wind"), skipChecks()),
				new CustomAnnotation(SimulatedWeatherSource.class.getDeclaredField("precipitation"), skipChecks()),
				new CustomAnnotation(SimulatedWeatherSource.class.getDeclaredField("temperature"), skipChecks()));
		} catch (NoSuchFieldException | SecurityException e) {
			System.out.println("failed on annotating " + e.getMessage());
			return Collections.emptyList();
		}
	}

	private SkipChecks skipChecks() {
		return new SkipChecks() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return SkipChecks.class;
			}
		};
	}

	@Override
	public Class<? extends TestTemplate> template() {
		return JUnit5TestTemplate.class;
	}

}
