# Remote Agent Attacher

### How to use

* Start the Java application you want to attach to.
* Have the name of its main class in mind or find the running application's process id (pid)
* Compile your agent to a jar file (agent-jar)
* Run `java de.andrena.justintime.attaching.RemoteAgentAttacher <agent-jar> <main-class-or-process-id> [<action>]`
  * `<agent-jar>` is the jar containing your java agent
  * `<main-class-or-process-id>` is the main class of the Java process (name parts will be sufficient) to attach to, or alternatively, the process id. If no match is found, a list of all running JVMs is printed.
  * `<action>` is either `attach` to attach a new agent or `detach` to detach an existing one. Default: `attach`. 
* For our 3 example agents, there are already predefined eclipse run configurations in this directory

### Configuring eclipse:
 
 * Make sure to have the classpath variable `M2_REPO` (pointing to your local m2 repository) defined
 * Also define a string substitution for `M2_REPO` (pointing to your local m2 repository)
 
 ### Configuring other IDEs:
 
 * Feel free to post a configuration that helps starting the project
 
 