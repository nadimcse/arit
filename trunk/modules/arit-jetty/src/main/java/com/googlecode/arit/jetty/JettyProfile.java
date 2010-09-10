package com.googlecode.arit.jetty;

import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.jetty.webapp.WebAppContext;

import com.googlecode.arit.ServerProfile;

public class JettyProfile implements ServerProfile {
    public String identifyApplication(ClassLoader classLoader) {
        if (classLoader instanceof WebAppClassLoader) {
            WebAppClassLoader wacl = (WebAppClassLoader)classLoader;
            WebAppContext context = (WebAppContext)wacl.getContext();
            return context.getContextPath();
        } else {
            return null;
        }
    }
}