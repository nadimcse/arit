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
package com.googlecode.arit.threads.ibm;

import java.util.TimerTask;

import org.codehaus.plexus.component.annotations.Component;

import com.googlecode.arit.rbeans.RBeanFactory;
import com.googlecode.arit.rbeans.RBeanFactoryException;
import com.googlecode.arit.threads.AbstractTimerThreadInspectorPlugin;
import com.googlecode.arit.threads.ThreadInspectorPlugin;

@Component(role=ThreadInspectorPlugin.class, hint="ibm-timer")
public class IBMTimerThreadInspectorPlugin extends AbstractTimerThreadInspectorPlugin {
    private final RBeanFactory rbf;

    public IBMTimerThreadInspectorPlugin() {
        RBeanFactory rbf;
        try {
            rbf = new RBeanFactory(TimerImplRBean.class);
        } catch (RBeanFactoryException ex) {
            rbf = null;
        }
        this.rbf = rbf;
    }

    public boolean isAvailable() {
        return rbf != null;
    }

    @Override
    protected TimerTask[] getTimerTasks(Thread thread) {
        if (rbf.getRBeanInfo(TimerImplRBean.class).getTargetClass().isInstance(thread)) {
            return rbf.createRBean(TimerImplRBean.class, thread).getTasks().getTimers();
        } else {
            return null;
        }
    }
}
