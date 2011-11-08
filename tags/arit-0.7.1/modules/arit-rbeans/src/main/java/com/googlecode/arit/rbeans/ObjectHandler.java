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
package com.googlecode.arit.rbeans;

/**
 * Converts an object returned by a target object to an object returned by an RBean. With the
 * exception of {@link PassThroughHandler}, the returned will be an RBean.
 * 
 * @author Andreas Veithen
 */
public abstract class ObjectHandler {
    public final Object handle(Object object) {
        return object == null ? null : doHandle(object);
    }
    
    protected abstract Object doHandle(Object object);
}
