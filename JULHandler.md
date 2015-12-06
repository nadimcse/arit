# java.util.logging (JUL) handlers #

## Introduction ##

Starting with Java 1.4 the JRE provides its own logging framework (`java.util.logging`, or "JUL" for short). As with other logging frameworks such as log4j, JUL allows to register handlers (implementations of `java.util.logging.Handler`) to receive and process log messages, e.g. to write them to a particular log file location.

Since JUL is built into the JRE, registering a handler in general changes the logging behavior of all the code running in the VM instance. Doing this in a J2EE environment may also create a class loader leak if the application that registers its own handler (with an implementation loaded from the application class loader), but fails to unregister it when the application is stopped.

Since registering a JUL handler may have unexpected side effects, it is normally not allowed in a J2EE environment, and an attempt to do that will result in an error if Java 2 Security is enabled. Indeed, registering a handler requires `LoggingPermission("control")`, which is not granted by the default policy for J2EE applications.

Starting with version 0.6, Arit reports JUL handlers registered by a J2EE application and identifies class loader leaks caused by applications not unregistering them when stopping.

## Tomcat and JULI ##

The description given above strictly only applies to runtime environments that use the default `java.util.logging.LogManager`. This is the case e.g. for WebSphere Application Server. Things are different for containers that configure their own `LogManager`. An example is Tomcat which installs its own implementation called [JULI](http://tomcat.apache.org/tomcat-6.0-doc/logging.html). This implementation isolates the logging configuration between different Web applications and performs automatic cleanup when an application is stopped.

When JULI is used, an application may safely install its own JUL handlers (using the standard `java.util.logging` API) without impacting other applications. In addition, because of the automatic cleanup performed by Tomcat, a failure to unregister the handler will not cause a class loader leak (although it is obviously still a good practice to unregister the handlers explicitly).

Starting with version 0.7, Arit detects the presence of JULI and adapts its own configuration:
  * The standard JUL plugin is disabled (since it would only inspect the logging configuration of Arit itself).
  * Instead, it will report JULI per-application logging contexts and any handlers registered in these contexts.

## `SLF4JBridgeHandler` and WebSphere ##

As noted above, WebSphere doesn't isolate the JUL configuration per application. This means that one should be very careful before registering JUL handlers, as this may have unexpected consequences:
  * It will modify the logging behavior not only for the application that registers the handler, but for the entire VM, including the WebSphere runtime. The reason is that the logging subsystem in WebSphere ultimately routes all log messages through JUL.
  * In some particular circumstances, this may result in a deadlock that quickly results in the entire server becoming unresponsive. More precisely this may occur when the following two conditions are met:
    * An exception is logged and the code that formats the exception message (i.e. the code in `getLocalizedMessage`) attempts to log a message itself. This has been observed in a real world application where `getLocalizedMessage` invokes code to load a message bundle and where that attempt results in a warning message being logged.
    * The handler (or some code invoked by the handler) attempts to acquire a lock (monitor). Since the default handler installed by WebSphere also uses a lock, one may end up in a deadlock situation where one thread holds the lock required by the WebSphere handler and waits for the lock required by the custom handler and another thread holds the lock required by the custom handler and waits for the lock required by the WebSphere handler. This has been observed on a real world WebSphere instance where an application had registered a JUL handler delegating to log4j.
  * Finally, if the application fails to unregister the handler, then this will result in a class loader leak because in contrast to Tomcat, WebSphere doesn't perform automatic cleanup in this case.

One frequently used custom JUL handler is `SLF4JBridgeHandler`. As explained in the [Javadoc](http://www.slf4j.org/api/org/slf4j/bridge/SLF4JBridgeHandler.html), the purpose of this handler is to redirect messages logged through JUL and to reinject them into SLF4J. Arit (as of version 0.6) detects the presence of this handler and displays the following line in the report:

  * `JUL handler org.slf4j.bridge.SLF4JBridgeHandler registered on logger <root>`

If you encounter this in a WebSphere environment, you should definitely fix the application in order to avoid the issues described above.

One example where this handler gets registered without the developer being aware of it is when using Grails. To avoid this, make sure that `grails.logging.jul.usebridge` is set to `false` in `Config.groovy`. As noted in [GRAILS-6778](http://jira.grails.org/browse/GRAILS-6778), this is anyway the recommended setting in a production environment.