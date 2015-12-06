# Threads and timers #

## Introduction ##

Although the J2EE specification forbids applications to create unmanaged threads, they are commonly used, especially in Web applications. They are also a common source of class loader leaks. These leaks occur when the application fails to stop the threads it has started during its lifetime. Indeed, by definition they are unmanaged and the container has no way of identifying them and stopping them on behalf of the application.

In some cases a thread owned by the container or the JRE may also accidentally get linked to an application class loader. This frequently happens with pooled threads or threads created lazily by the container in response to an action of the application. An example of this situation is shown below.

A thread may be linked to an application in three different ways:
  1. Through its implementation class (subclass of `Thread`), target class (implementation of the `Runnable` interface) or `TimerTask` implementation (for timers). This class defines the logic executed in the thread or timer and is typically loaded from the application class loader. As long as the thread is running, it will hold a strong reference to the application class loader via this class.
  1. Through its context class loader. Each thread has an associated class loader that will be used by various APIs to dynamically load additional classes. E.g. JMX uses the thread context class loader to deserialize non primitive return values from a remote MBean invocation. A new thread automatically inherits the context class loader of the thread from which the new thread was created. The context class loader may change over time. E.g. in a J2EE environment, the container will automatically set the thread context class loader to the application class loader before invoking a servlet or an EJB. This means that an unmanaged thread created by a J2EE application will generally have its context class loader set to the class loader of the application that created it.
  1. Through its access control context. In an environment where Java 2 security is enabled, this determines the initial set of permissions that the code running in the thread will have. Since obviously a thread must not have more permissions than the code that created it, the access control context is inherited as well. It is actually determined by the call stack at the moment the thread is created and keeps references to the class loaders for the classes on the call stack.

## Identifying class loader leaks caused by the container ##

As mentioned above, invocations of certain APIs exposed by the container or the JRE may have as a side effect the creation of one or more new threads. Examples include:
  * The platform MBean server is instantiated lazily when the first MBean is registered or when the first connection is made. Some containers use the platform MBean server to expose their own MBeans, so that it will already have been instantiated when the first application is started. This is the case for Tomcat. Other containers such as WebSphere register their MBeans in an MBean server other than the platform MBean server. This means that the instantiation of the platform MBean server may actually be triggered by an application when it attempts to register a custom MBean. On most JREs, this will also create one or more threads, which are used e.g. to handle JMX notifications.
  * A remote JNDI lookup or a remote EJB invocation may cause the creation of a new connection (if no pooled connection is available) and there may be a thread linked to this connection. E.g. the ORB shipped with WebSphere will create a reader thread for each new connection.
  * The first invocation of certain parts of the AWT and Swing APIs will create various cleanup threads.

To avoid a class loader leak when creating such threads, the container (or code in the JRE) must do two thinks:
  1. The code that creates the thread must be executed with `AccessController#doPrivileged`. This is necessary anyway in order to support Java 2 Security, because otherwise the thread creation would fail if Java 2 Security is enabled and the application (that indirectly triggers the creation of the thread) doesn't have `RuntimePermission("modifyThread")`. Usage of `AccessController#doPrivileged` also makes sure that the access control context of the thread doesn't contain any strong reference to the application class loader.
  1. It must make sure that the context class loader of the thread doesn't refer to the application class loader. The container can do this by setting the context class loader explicitly on the new thread or by temporarily changing the context class loader of the current thread before creating the new thread.

If the container fails to meet one of these expectations, then the newly created thread will keep a strong reference to the application class loader and prevent it from being garbage collected when the application is stopped. An example is shown in the following screenshot:

![http://arit.googlecode.com/svn/wiki/MemoryNotificationThread.png](http://arit.googlecode.com/svn/wiki/MemoryNotificationThread.png)

The application simply registers an MBean in the platform MBean server. What the screenshot shows is that after restarting the application, the class loader of the stopped instance is leaked because there is a running thread (as well as a shutdown hook) that keeps a strong reference to the application class loader. From the class name of the thread implementation it is easy to see that this thread was created by the JRE. The screenshot also shows that the thread is linked to the application class loader only through its context class loader, but not its access control context.

This analysis is confirmed by setting a breakpoint in the constructor of the problematic thread class and inspecting the stack trace:

![http://arit.googlecode.com/svn/wiki/MemoryNotificationThread-stacktrace.png](http://arit.googlecode.com/svn/wiki/MemoryNotificationThread-stacktrace.png)

One can see that the thread is indeed created as a side effect of the (first) invocation of `ManagementFactory#getPlatformMBeanServer()`. One can also see that the JRE correctly uses `AccessController#doPrivileged` to execute the code that creates the thread. However, it fails to set the correct context class loader on this thread.