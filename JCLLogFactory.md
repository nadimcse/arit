# Cached JCL LogFactory instances #

## Introduction ##

[Commons logging](http://commons.apache.org/logging/) (formerly known as Jakarta Commons Logging, or JCL for short) has often been blamed for causing class loader leaks, and competing APIs such as SLF4J have tried to derive some of their legitimacy from these alleged flaws in JCL. However, there is really only one setup where using JCL may cause a class loader leak. In fact, this occurs when both of the following two conditions are met:
  * Commons logging is installed into the server runtime although the application server has no explicit support for this configuration. Indeed most servers have a mechanism that allows to add arbitrary JARs to a shared class loader from were they become visible to all applications (or a configurable subset of applications). E.g. in Tomcat 6.0 this is done by placing the JARs into the `$CATALINA_HOME/lib` directory.
  * The application either uses parent-first class loading or it uses parent-last class loading and the JCL library is **not** included in the application itself. It should be noted that this situation is actually in contradiction with the recommendations made by the J2EE specification:
    * Section SRV.9.7.2 of the Java Servlet Specification Version 2.4 recommends "that the application class loader be implemented so that classes and resources packaged within the WAR are loaded in preference to classes and resources residing in container-wide library JARs." This basically implies that parent-last class loading should be used (with special provisions for classes in `java.*` and `javax.*`).
    * Not packaging the JCL library in the application would mean that the application makes use of an API that is not part of the J2EE specification (and that a container is not required to provide) but doesn't contain the necessary classes. This makes the application dependent on a particular server product or configuration.

If an environment that is set up like this, an application will actually invoke the JCL classes deployed higher up in the class loader hierarchy. This will indeed result in a class loader leak. The reason is that JCL maintains a per class loader cache of `LogFactory` instances. Entries in this cache keep references to the application class loaders and for reasons explained [here](http://wiki.apache.org/commons/Logging/UndeployMemoryLeak) these entries can't be garbage collected when the application is stopped, resulting in a class loader leak.

As pointed out above, the particular setup in which this issue occurs is actually in contradiction with the J2EE specification and should therefore be considered as broken.

## WebSphere ##

With respect to JCL, WebSphere environments are particular for two reasons:
  * Applications deployed in WebSphere use parent-first class loading by default (which could be consider as being in contradiction with the J2EE specification).
  * The WAS runtime already provides a version of JCL which is preconfigured such that by default, all messages logged with JCL will be routed through WebSphere's logging subsystem.

However, this doesn't mean that a WebSphere environment satisfies the two conditions enumerated above. In fact, since WebSphere provides JCL support out of the box, it also takes the necessary measures to clean up the class loader references held by JCL when an application is stopped. This normally avoids the class loader leak issue described above.

In addition, in WAS 7.0 the JCL library is also included in the OSGi bundle that contains the JAX-WS implementation (Axis2). In versions before 7.0.0.19, the `LogFactory` cache maintained by this instance was not managed properly by the WAS runtime, which means that applications using JAX-WS caused class loader leaks. This problem is described by APAR [PM36842](http://www-01.ibm.com/support/docview.wss?uid=swg1PM36842) and is fixed in 7.0.0.19. The `LogFactory` cache now uses a `WeakHashMap`. Since in addition, it is configured such that it will always use `java.util.logging` (and never a logging implementation provided by the application), this indeed fixes the class loader leak. This also implies that Arit may display a resource with description _LogFactory instance cached by JAX-WS (Axis2) runtime_ on a stopped application. Since this effectively represents a weak reference, it means that there is a class loader leak caused by something else.

## Detection in Arit ##

If JCL is deployed into the container, then Arit will report any entries in the `LogFactory` cache. On WAS 7.0, Arit also reports class loader references held by the JCL library included in the JAX-WS runtime.

## What to do with class loader leaks caused by cached `LogFactory` instances? ##

If you have added JCL yourself to the server runtime, then you should fix your configuration and remove it. Instead you should package JCL with the application.

If JCL is shipped with your application server, then a class loader leak involving a cached `LogFactory` instance indicates that the cleanup performed by the application server was not effective. This is not necessary the fault of the server. In these situations, enable the [JCL diagnostic logging](http://commons.apache.org/logging/troubleshooting.html) to determine why there is still an entry in the `LogFactory` cache after the application is stopped.