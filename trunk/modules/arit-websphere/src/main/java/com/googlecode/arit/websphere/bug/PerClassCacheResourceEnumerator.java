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
package com.googlecode.arit.websphere.bug;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.googlecode.arit.ResourceEnumerator;
import com.googlecode.arit.ResourceType;

public abstract class PerClassCacheResourceEnumerator implements ResourceEnumerator {
    private final ResourceType resourceType;
    private final Iterator<Class<?>> classIterator;
    private Class<?> clazz;

    public PerClassCacheResourceEnumerator(ResourceType resourceType, Map<Class<?>,?> cache) {
        this.resourceType = resourceType;
        classIterator = cache.keySet().iterator();
    }

    public final ResourceType getType() {
        return resourceType;
    }

    public final Collection<ClassLoader> getClassLoaders() {
        return Collections.singleton(clazz.getClassLoader());
    }

    public final String getDescription() {
        return getDescription(clazz);
    }
    
    protected abstract String getDescription(Class<?> clazz);
    
    public final boolean next() {
        if (classIterator.hasNext()) {
            clazz = classIterator.next();
            return true;
        } else {
            return false;
        }
    }
}