# Release notes for 0.7.4 #

Minor change to add the module/classloader ID to the HTML report.

# Release notes for 0.7.3 #

Maintenance release that adds compatibility with recent IBM Java 6 versions (in particular the version that comes with WAS 8.5.0.2) and Oracle Java 7.

# Release notes for 0.7.2 #

Maintenance release that fixes some minor bugs and introduces an additional WebSphere feature (detection of cached servant objects).

# Release notes for 0.7.1 #

Minor bug fix release that solves an issue that occurs with particular WAS 7.0 setups.

# Release notes for 0.7 #

  * Bug fixes and improved support for java.util.logging, including support for JULI (i.e. Tomcat's JUL implementation).
  * Improved support for Jetty 6.
  * Support for Axis2 modules (MAR files) and services archives (AAR files).
  * Arit now uses Spring instead of Plexus as IoC container.
  * Various minor UI improvements.
  * Better support for applications that create custom class loaders.
  * Improved inspection of Thread objects.
  * The leak detector now writes a report to the logs when a leak is detected (in addition to generating a JMX notification).
  * The RHQ plugin now supports per application statistics.
  * Arit now detects leaks caused by commons-discovery (when deployed in the server runtime).
  * Various WebSphere specific enhancements.

# Release notes for 0.6 #

The following features have been added in this release:
  * Complete support for WAS 6.1.
  * Support for Tomcat 6.0.
  * Reports and statistics about class loader leaks can now be retrieved using JMX.
  * Initial support for reports in XML.
  * Support for the following new resource types has been added:
    * Java 2 security managers
    * TCP socket acceptor threads
    * Resource types for various WebSphere bugs
    * Factories cached by commons-logging
    * Thread groups
    * java.util.logging handlers
  * Identification of threads and shutdown hooks that keep strong references to an application class loader through AccessControlContext.
  * Initial support for resource cleanup, i.e. removing strong references to stopped application instances.
  * Improved identification of the value classes of a ThreadLocal. This e.g. allows to identify applications affected by bugs such as AXIS-2674.
  * If an application was built using Maven, its group ID, artifact ID and version are now displayed in the report.
  * The HTML report now shows how resources are linked to applications class loaders. E.g. a thread may be linked to a class loader in four different ways: by its implementation class, target (Runnable), context class loader or access control context.
  * Initial version of a plugin for the RHQ monitoring platform. Right now it simply allows to measure the number of detected class loader leaks.

Known issues and limitations:
  * Arit will not run on WAS 7.0 with a fix pack level lower than 7.0.0.7.
  * On WAS 7.0, if Java 2 security is enabled, a SECJ0314W warning may appear in `SystemOut.log` when Arit starts. This is caused by an unprivileged file access in the `DriverManager#loadDriversFromFiles` method (which is executed when the `DriverManager` class is initialized). In most cases, this warning can be safely ignored. However, the issue may prevent loading of JDBC drivers that use `META-INF/services/java.sql.Driver` to register themselves.

# Release notes for 0.5 #

The following features have been added in this release:
  * (Partial) support for WAS 6.1.
  * Support for Apache Harmony 5.0 and 6.0 JDKs.
  * Arit can now be deployed on a WAS 7.0 with Java 2 security enabled.
  * The following new resource types have been added:
    * RMI exports
    * Cached ResourceBundle instances
    * JCE providers
  * Arit now exposes an MBean that sends out notifications about detected leaks.
  * For EAR files built using Maven, the artifact identifier (groupId, artifactId, version) is shown in the report (WAS 7.0 only).

This release also solves the redirection issue, i.e. the report can accessed using `/arit`.

Known issues and limitations:
  * Arit will not run on WAS 7.0 with a fix pack level lower than 7.0.0.7.
  * On WAS 6.1, some icons are rendered incorrectly (black opaque background instead of transparency).
  * On WAS 7.0, if Java 2 security is enabled, a SECJ0314W warning may appear in `SystemOut.log` when Arit starts. This is caused by an unprivileged file access in the `DriverManager#loadDriversFromFiles` method (which is executed when the `DriverManager` class is initialized). In most cases, this warning can be safely ignored. However, the issue may prevent loading of JDBC drivers that use `META-INF/services/java.sql.Driver` to register themselves.

# Release notes for 0.4 #

This release is primarily targeted at WebSphere 7.0 and adds the following new features:
  * A graphical view that reproduces the class loader hierarchy and that shows the status (started or stopped/defunct) of each module, so that leaks can be identified easily.
  * Arit now runs on any Java version, regardless of the version on which it has been built.
  * Detection of a particular kind of memory leak that occurs on WAS 7.0 (see APAR IZ67457).

Known issues and limitations:
  * Arit will not run on WAS 7.0 with a fix pack level lower than 7.0.0.7.
  * Redirection is not properly working in Arit 0.4. To access the report, use `/arit/report.html` instead of `/arit`.

# Release notes for 0.3 #

This release adds support for MBeans. In addition, there have been two important architectural changes:
  * Arit now uses the Plexus container.
  * The concept of _RBean_ has been introduced to simplify reflection.

**Important notice for WebSphere users**: This release requires at least version 7.0.0.7 because Plexus is impacted by APAR PK86109.

Due to a bug, version 0.3 will only run correctly on the JDK on which it has been built, i.e. the EAR or WAR built with Java 1.5 will not run on Java 1.6 and vice versa. Note that the EAR in the download section has been built with 1.6.