/*
 * Copyright 2010 Andreas Veithen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.arit.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import com.googlecode.arit.ProviderFinder;
import com.googlecode.arit.ResourceEnumerator;
import com.googlecode.arit.ResourceEnumeratorFactory;
import com.googlecode.arit.ServerContext;
import com.googlecode.arit.ServerProfile;
import com.googlecode.arit.ServerProfileFactory;

public class InspectorServlet extends HttpServlet {
    private PlexusContainer container;
    private ServerProfile profile;
    private List<ResourceEnumeratorFactory> resourceEnumeratorFactories;
    
    private ServerContext getServerContext() {
        return new ServerContext(getServletContext(), getClass().getClassLoader());
    }
    
    @Override
    public void init() throws ServletException {
        try {
            container = new DefaultPlexusContainer();
            ServerContext serverContext = getServerContext();
            for (ServerProfileFactory spf : container.lookupList(ServerProfileFactory.class)) {
                profile = spf.createServerProfile(serverContext);
                if (profile != null) {
                    break;
                }
            }
        } catch (PlexusContainerException ex) {
            throw new ServletException(ex);
        } catch (ComponentLookupException ex) {
            throw new ServletException(ex);
        }
        resourceEnumeratorFactories = ProviderFinder.find(ResourceEnumeratorFactory.class);
    }

    @Override
    public void destroy() {
        container.dispose();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (profile == null) {
            request.setAttribute("serverContext", getServerContext());
            request.getRequestDispatcher("/WEB-INF/view/noprofile.jspx").forward(request, response);
        } else {
            List<Application> applications = new ArrayList<Application>();
            Map<ClassLoader,Application> classLoaderMap = new IdentityHashMap<ClassLoader,Application>();
            for (ResourceEnumeratorFactory resourceEnumeratorFactory : resourceEnumeratorFactories) {
                ResourceEnumerator resourceEnumerator = resourceEnumeratorFactory.createEnumerator();
                while (resourceEnumerator.next()) {
                    for (ClassLoader classLoader : resourceEnumerator.getClassLoaders()) {
                        if (classLoader != null) {
                            Application application;
                            if (classLoaderMap.containsKey(classLoader)) {
                                application = classLoaderMap.get(classLoader);
                            } else {
                                String appName = profile.identifyApplication(classLoader);
                                if (appName == null) {
                                    application = null;
                                } else {
                                    application = new Application(appName);
                                    applications.add(application);
                                }
                                classLoaderMap.put(classLoader, application);
                            }
                            if (application != null) {
                                application.getResources().add(new Resource(resourceEnumerator.getDescription()));
                                break;
                            }
                        }
                    }
                }
            }
            Collections.sort(applications, new Comparator<Application>() {
                public int compare(Application o1, Application o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            request.setAttribute("factories", resourceEnumeratorFactories);
            request.setAttribute("applications", applications);
            request.getRequestDispatcher("/WEB-INF/view/resources.jspx").forward(request, response);
        }
    }
}