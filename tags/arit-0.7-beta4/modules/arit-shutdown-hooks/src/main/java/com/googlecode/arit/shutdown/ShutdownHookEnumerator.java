/*
 * Copyright 2010-2011 Andreas Veithen
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
package com.googlecode.arit.shutdown;

import java.util.Iterator;
import java.util.List;

import com.googlecode.arit.Formatter;
import com.googlecode.arit.ResourceType;
import com.googlecode.arit.threadutils.ThreadHelper;
import com.googlecode.arit.threadutils.ThreadObjectEnumerator;

public class ShutdownHookEnumerator extends ThreadObjectEnumerator {
    private final ResourceType resourceType;
    private final Iterator<Thread> iterator;
    
    public ShutdownHookEnumerator(ResourceType resourceType, List<Thread> hooks, ThreadHelper threadHelper) {
        super(threadHelper);
        this.resourceType = resourceType;
        iterator = hooks.iterator();
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public String getResourceDescription(Formatter formatter) {
        return "Shutdown hook; type=" + threadObject.getClass().getName();
    }

    protected Thread nextThreadObject() {
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }

    @Override
    protected boolean nextOtherClassLoaderReference() {
        return false;
    }

    @Override
    protected ClassLoader getOtherReferencedClassLoader() {
        return null;
    }

    @Override
    protected String getOtherClassLoaderReferenceDescription() {
        return null;
    }

    public boolean cleanup() {
        return false;
    }
}