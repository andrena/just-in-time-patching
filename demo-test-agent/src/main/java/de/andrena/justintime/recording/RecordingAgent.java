package de.andrena.justintime.recording;

import java.lang.instrument.Instrumentation;

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

	public static volatile RecordingAgent agent;

	public RecordingAgent(Instrumentation instrumentation) {
		super(instrumentation, configure());
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
		if (arg.equals("detach")) {
			System.out.println("detaching");
			if (agent != null) {
				System.out.println("agent to detach exists");
				agent.shutdown();
				agent = null;
			} else {
				System.out.println("no agent to detach exists");
			}
		} else {
			System.out.println("attaching");
			agent = new RecordingAgent(instrumentation);
			agent.prepareInstrumentations();
		}
	}

	public void shutdown() {
		clearInstrumentations();
	}
}
