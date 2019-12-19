package de.andrena.justintime.recording;

import java.nio.file.Paths;

import net.amygdalum.testrecorder.generator.ScheduledTestGenerator;
import net.amygdalum.testrecorder.profile.AgentConfiguration;

public class MyTestGenerator extends ScheduledTestGenerator {

    public MyTestGenerator(AgentConfiguration config) {
        super(config);
        this.generateTo = Paths.get("F:\\javaworkspace\\remote-agent-demo\\demo-test-agent/target/generated");
		this.counterInterval = 2;
		this.classNameTemplate = "${class}${counter}Test";
        this.dumpOnShutdown(true);
    }

}
