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
package com.googlecode.arit.shutdown;

import java.util.List;

import org.codehaus.plexus.PlexusTestCase;

public class ShutdownHookInspectorTest extends PlexusTestCase {
    public void test() throws Exception {
        ShutdownHookInspectorManager inspectorManager = lookup(ShutdownHookInspectorManager.class);
        assertTrue(inspectorManager.isAvailable());
        Thread hook = new Thread();
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(hook);
        List<Thread> hooks = inspectorManager.getInspector().getShutdownHooks();
        assertTrue(hooks.contains(hook));
        runtime.removeShutdownHook(hook);
    }
}
