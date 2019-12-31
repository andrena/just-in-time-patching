# Remote Agent Demo

* Start the Java Application you want to attach to.
* Find the process id of the started java application (pid)
* Compile your agent to a jar file (agent-jar)
* Run `java de.andrena.justintime.attaching.RemoteAgentAttacher <agent-jar> <main-class> [<action>]`
  * <agent-jar> is a jar containing a java agent
  * <main-class> is the main class of the java process (name parts will be sufficient) to attach to (a list is printed if no match found)
  * <action> `attach` a new agent or `detach` an existing one 
