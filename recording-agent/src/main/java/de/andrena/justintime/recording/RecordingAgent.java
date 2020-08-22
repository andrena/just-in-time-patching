package de.andrena.justintime.recording;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

import net.amygdalum.testrecorder.TestRecorderAgent;
import net.amygdalum.testrecorder.configurator.AgentConfigurator;
import net.amygdalum.testrecorder.profile.AgentConfiguration;

public class RecordingAgent extends TestRecorderAgent {

	public static volatile List<RecordingAgent> agents = new ArrayList<>();

	public RecordingAgent(Instrumentation instrumentation) {
		super(instrumentation, configure());
		this.prepareInstrumentations();
		
	}

	private static AgentConfiguration configure() {
		return new AgentConfigurator()
			.defaultSerializers()
			.customSerializer(LocalDateTimeSerializer::new)
			.defaultSetupGenerators()
			.customSetupGenerator(LocalDateTimeSetupGenerator::new)
			.defaultMatcherGenerators()
			.customMatcherGenerator(LocalDateTimeMatcherGenerator::new)
			.generateTests(MyTestGeneratorProfile::new)
			.record(MySerializationProfile::new)
			.to(MyTestGenerator::new)
			.configure();
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
