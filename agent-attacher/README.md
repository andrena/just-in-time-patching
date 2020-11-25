# Remote Agent Attacher

### How to use

* Start the Java application you want to attach to.
* Have the name of its main class in mind or find the running application's process id (pid)
* Compile your agent to a jar file (agent-jar)
* Run `java de.andrena.justintime.attaching.RemoteAgentAttacher <agent-jar> <main-class-or-process-id> [<action>]`
  * `<agent-jar>` is the jar containing your java agent
  * `<main-class-or-process-id>` is the main class of the Java process (name parts will be sufficient) to attach to, or alternatively, the process id. If no match is found, a list of all running JVMs is printed.
  * `<action>` is either `attach` (default) to attach a new agent or `detach` to detach an existing one. 
* For our 3 example agents, you find already predefined eclipse run configurations in this directory

### Required Java Version

This example requires exactly Java 8 (smaller or larger versions will not work).

### Configuring eclipse:
 
 * Make sure that you have m2e installed
 * Make sure to have the classpath variable `M2_REPO` (pointing to your local m2 repository) defined
 * Also define a string substitution for `M2_REPO` (pointing to your local m2 repository)
 * Make sure that have an entry for a <java 8 jdk> (not jre!) in your "Installed JREs"
 * Make sure that the execution environment "JavaSE-1.8" is mapped to <java 8 jdk>
 * Edit the JRE system libraries for the <java 8 jdk> to contain also the tools.jar file from the lib directory of the jdk

 ### Configuring other IDEs:
 
 * Feel free to post a configuration that helps starting the project
 
 