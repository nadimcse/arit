# Installing Arit #

## Supported runtime environments and application servers ##

As explained in the [introduction](GettingStarted.md), Arit relies on implementation specific code to detect certain types of class loader leaks. This means that Arit will only run on Java runtime environments and application servers that are explicitly supported.

For the moment, the following JREs are supported:
  * Oracle Java 1.5 and 1.6, including the version distributed with Mac OS X.
  * IBM Java 1.5 and 1.6.

Arit used to support (and probably still does) Apache Harmony 1.5 and 1.6. However, because of Oracle's denial to grant an acceptable Java SE JCK license to the Harmony project, support for this runtime environment is no longer a goal of the Arit project.

Arit is most actively tested and developed on WebSphere 6.1 and 7.0. Arit also supports Tomcat and Jetty. No other application servers or servlet containers are currently supported. It is planned to add at least support for Apache Geronimo and JBoss in the future.

## Deploying Arit ##

Arit is a simple Web application that doesn't require any additional configuration of the container. Simply deploy the EAR or WAR from the binary distribution into your application server or servlet container.

## Accessing the Arit report ##

To access the Arit report in HTML form, navigate to the root of the Web application context. If Arit has been deployed using the EAR file, then the context root should be `/arit`. You will be redirected to `report.html` which displays the report.