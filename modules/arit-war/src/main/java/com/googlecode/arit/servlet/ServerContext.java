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

import javax.servlet.ServletContext;

// TODO: eliminate this
public class ServerContext {
    private final ServletContext servletContext;
    private final ClassLoader applicationClassLoader;
    
    public ServerContext(ServletContext servletContext, ClassLoader applicationClassLoader) {
        this.servletContext = servletContext;
        this.applicationClassLoader = applicationClassLoader;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public ClassLoader getApplicationClassLoader() {
        return applicationClassLoader;
    }
}
