package de.andrena.justintime.recording;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

import net.amygdalum.testrecorder.DefaultPerformanceProfile;
import net.amygdalum.testrecorder.DefaultSerializationProfile;
import net.amygdalum.testrecorder.DefaultSnapshotConsumer;
import net.amygdalum.testrecorder.TestRecorderAgent;
import net.amygdalum.testrecorder.profile.AgentConfiguration;
import net.amygdalum.testrecorder.profile.ClassPathConfigurationLoader;
import net.amygdalum.testrecorder.profile.DefaultPathConfigurationLoader;
import net.amygdalum.testrecorder.profile.PerformanceProfile;
import net.amygdalum.testrecorder.profile.SerializationProfile;
import net.amygdalum.testrecorder.profile.SnapshotConsumer;

public class RecordingAgent extends TestRecorderAgent {

	public static volatile List<RecordingAgent> agents = new ArrayList<>();

	public RecordingAgent(Instrumentation instrumentation) {
		super(instrumentation, configure());
		this.prepareInstrumentations();
	}

	private static AgentConfiguration configure() {
		AgentConfiguration config = new AgentConfiguration(new ClassPathConfigurationLoader(), new DefaultPathConfigurationLoader())
			.withDefaultValue(SerializationProfile.class, DefaultSerializationProfile::new)
			.withDefaultValue(PerformanceProfile.class, DefaultPerformanceProfile::new)
			.withDefaultValue(SnapshotConsumer.class, DefaultSnapshotConsumer::new);
		return config;
	}

	public static void premain(String arg, Instrumentation instrumentation) {
		agentmain(arg, instrumentation);
	}

	public static void agentmain(String arg, Instrumentation instrumentation) {
		if (arg.equals("attach")) {
			RecordingAgent agent = new RecordingAgent(instrumentation);
			System.out.println("attaching RecordingAgent " + System.identityHashCode(agent));
			agents.add(agent);
		} else if (arg.equals("detach")) {
			if (agents.isEmpty()) {
				System.out.println("there is no RecordingAgent to detach");
			}
			for (RecordingAgent agent : agents) {
				System.out.println("detaching RecordingAgent " + System.identityHashCode(agent));
				agent.shutdown();
			}
			agents.clear();
		} else {
			System.out.println("Invalid argument for attaching/detaching RecordingAgent");
		}
	}

	public void shutdown() {
		clearInstrumentations();
	}
}
