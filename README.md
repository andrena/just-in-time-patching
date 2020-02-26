Just-in-Time Patching Java Applications
=======================================

This project contains the source code to a presentation about Just-in-Time Patching Java Applications.

We start with a [demo application](https://github.com/andrena/just-in-time-patching/tree/master/demo-application) containing a defect:

* start the applications main class
* the application will start on `http://localhost:8080` 
* navigate throught the dates with the indicators
* select a friday 13th in the future and update your browser some times

Now you can patch this code without restarting the application:

* try out the insertions of log statements into a broken method
  * build [log agent](https://github.com/andrena/just-in-time-patching/tree/master/log-agent) with maven
  * attach the agent with the [agent-attacher](https://github.com/andrena/just-in-time-patching/tree/master/agent-attacher)
* try out recording the states before and after method invocation (and generating tests from it)
  * build [recording agent](https://github.com/andrena/just-in-time-patching/tree/master/recording-agent) with maven
  * attach the agent with the [agent-attacher](https://github.com/andrena/just-in-time-patching/tree/master/agent-attacher)
* write your patch code or simply view how the defect could be fixed
  * build [patch agent](https://github.com/andrena/just-in-time-patching/tree/master/patch-agent) with maven
  * attach the agent with the [agent-attacher](https://github.com/andrena/just-in-time-patching/tree/master/agent-attacher)

## Feedback

Please provide feedback - especially if something does not work as you expect it.