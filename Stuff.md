# Starting/stopping application on Jetty #

Instead of deploying the WAR to the `webapps` folder, create a minimal context configuration and place it in the `contexts` folder:

```
<?xml version="1.0"?>
<Configure class="org.mortbay.jetty.webapp.WebAppContext">
  <Set name="contextPath">/test</Set>
  <Set name="war">/opt/test.war</Set>
</Configure>
```