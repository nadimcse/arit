# WebSphere #

  * [IZ67457](http://www-01.ibm.com/support/docview.wss?uid=swg1IZ67457): May affect any application running on WAS 7 with a JDK < SR8.
  * [PK83186](http://www-01.ibm.com/support/docview.wss?uid=swg1PK83186): Affects all applications using message driven beans running on WAS < 6.1.0.27 and WAS < 7.0.0.5.
  * [PM04639](http://www-01.ibm.com/support/docview.wss?uid=swg1PM04639): May randomly affect a single application on WAS < 6.1.0.33 and WAS < 7.0.0.15.
  * [PM18729](http://www-01.ibm.com/support/docview.wss?uid=swg1PM18729): Affects applications using JAX-RPC or JAX-WS on WAS < 6.1.0.35 and WAS < 7.0.0.13.
  * [PM21638](http://www-01.ibm.com/support/docview.wss?uid=swg1PM21638): Affects JSP and JSF applications running on WAS < 7.0.0.15.
  * [PM36842](http://www-01.ibm.com/support/docview.wss?uid=swg1PM36842): Although the APAR refers to SCA, the problem also affects JAX-WS applications on WAS < 7.0.0.19.
  * PM44455: This APAR described a class loader leak related to `org.apache.axis2.jaxws.description.DescriptionKey` objects. It has been cancelled by IBM because the original fix introduced a memory leak (due to an incorrect usage of `WeakHashMap`). Until now, IBM has not been able to come up with a proper fix for the issue.
  * [PM46493](http://www-01.ibm.com/support/docview.wss?uid=swg1PM46493): Affects all applications that use JAX-WS clients on WAS < 7.0.0.21.

# Axis 1.x #

  * [AXIS-2674](https://issues.apache.org/jira/browse/AXIS-2674): Affects all applications using Axis 1.4.