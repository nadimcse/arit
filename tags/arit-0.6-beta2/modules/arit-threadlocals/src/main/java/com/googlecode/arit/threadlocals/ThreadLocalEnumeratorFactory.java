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
package com.googlecode.arit.threadlocals;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import com.googlecode.arit.ResourceEnumerator;
import com.googlecode.arit.ResourceEnumeratorFactory;
import com.googlecode.arit.ResourceType;
import com.googlecode.arit.threadutils.ThreadUtils;

@Component(role=ResourceEnumeratorFactory.class, hint="threadlocal")
public class ThreadLocalEnumeratorFactory implements ResourceEnumeratorFactory {
    @Requirement(hint="threadlocal")
    private ResourceType resourceType;
    
    @Requirement
    private ThreadLocalInspector inspector;
    
    public boolean isAvailable() {
        return inspector.isAvailable();
    }

    public String getDescription() {
        return "Thread locals";
    }

    public ResourceEnumerator createEnumerator() {
        Map<ThreadLocal<?>,Set<Class<?>>> threadLocals = new IdentityHashMap<ThreadLocal<?>,Set<Class<?>>>();
        for (Thread thread : ThreadUtils.getAllThreads()) {
            for (Map.Entry<ThreadLocal<?>,Object> entry : inspector.getThreadLocalMap(thread).entrySet()) {
                ThreadLocal<?> threadLocal = entry.getKey();
                Object value = entry.getValue();
                if (value != null) {
                    Set<Class<?>> classes = threadLocals.get(threadLocal);
                    if (classes == null) {
                        classes = new HashSet<Class<?>>();
                        threadLocals.put(threadLocal, classes);
                    }
                    classes.add(value.getClass());
                }
            }
        }
        return new ThreadLocalEnumerator(resourceType, threadLocals);
    }
}
