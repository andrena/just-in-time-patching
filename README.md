# Remote Agent Demo

* Start the Java Application you want to attach to.
* Find the process id of the started java application (pid)
* Compile your agent to a jar file (agent-jar)
* Run `java net.amygdalum.testrecorder.remoteagent.RemoteAgentAttacher <agent-jar> <pid>`