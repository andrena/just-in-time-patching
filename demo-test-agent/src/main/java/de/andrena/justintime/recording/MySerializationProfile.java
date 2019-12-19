package de.andrena.justintime.recording;

import static java.util.Arrays.asList;

import java.util.List;

import net.amygdalum.testrecorder.DefaultSerializationProfile;
import net.amygdalum.testrecorder.profile.Classes;
import net.amygdalum.testrecorder.profile.Methods;

public class MySerializationProfile extends DefaultSerializationProfile {

	@Override
	public List<Classes> getClasses() {
		return asList(Classes.byName("TodoList"));
	}

	@Override
	public List<Methods> getRecorded() {
		return asList(Methods.byName("add"));
	}
	
}
