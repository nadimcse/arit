# Thread-locals #

A thread local will cause a class loader leak if the following conditions are met:
  1. There is a strong reference from the application class loader to the `ThreadLocal` object. This is the case in particular if the thread-local is stored in a static attribute.
  1. The thread-local values are instances of classes loaded from the application class loader.
  1. The thread-local is not cleared after a request to the application completes.

Arit is able to find `ThreadLocal` objects storing instances of classes loaded from application class loaders. If a thread-local is reported by Arit for a stopped application, then this means that the last two conditions are satisfied. However, Arit is not able to check the first condition, and the thread-local may actually be reported because another strong reference is preventing the class loader (and the thread-local) from being garbage collected. The only way to know with certainty whether a thread-local will create a class loader leak is to analyze a heap dump of the JVM or to inspect the code that creates the thread-local.

The following screenshot shows an example of an Arit report providing evidence for the class loader leak described by [AXIOM-354](https://issues.apache.org/jira/browse/AXIOM-354):

![http://arit.googlecode.com/svn/wiki/axiom-leak.png](http://arit.googlecode.com/svn/wiki/axiom-leak.png)

The test application shown in the report simply executed a call to `UIDGenerator.generateUID()`.