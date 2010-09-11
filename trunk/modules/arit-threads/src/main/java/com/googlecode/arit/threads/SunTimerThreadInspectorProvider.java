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
package com.googlecode.arit.threads;

import com.googlecode.arit.Provider;
import com.googlecode.arit.util.ReflectionUtil;

public class SunTimerThreadInspectorProvider implements Provider<ThreadInspector> {
    public ThreadInspector getImplementation() {
        try {
            Class<?> timerThreadClass = Class.forName("java.util.TimerThread");
            Class<?> taskQueueClass = Class.forName("java.util.TaskQueue");
            return new TimerThreadInspector(timerThreadClass,
                    ReflectionUtil.getField(timerThreadClass, "queue"),
                    ReflectionUtil.getField(taskQueueClass, "queue"));
        } catch (ClassNotFoundException ex) {
            return null;
        } catch (NoSuchFieldException ex) {
            return null;
        }
    }
}
