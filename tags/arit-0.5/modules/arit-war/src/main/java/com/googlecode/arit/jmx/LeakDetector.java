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
package com.googlecode.arit.jmx;

import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.JMException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Disposable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.googlecode.arit.ModuleStatus;
import com.googlecode.arit.report.Module;
import com.googlecode.arit.report.ReportGenerator;

@Component(role=LeakDetector.class)
public class LeakDetector extends NotificationBroadcasterSupport implements LeakDetectorMBean, Initializable, Disposable {
    private final static String LEAK_DETECTED = "arit.leak.detected";
    
    @Requirement
    private ReportGenerator reportGenerator;
    
    private MBeanServer mbs;
    private ObjectName registeredObjectName;
    private Timer timer;
    private final Set<Integer> reportedModules = new HashSet<Integer>();
    private long notificationSequence;
    
    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[] { new MBeanNotificationInfo(
                new String[] { LEAK_DETECTED }, Notification.class.getName(), "Leak detector notification") };
    }

    public void initialize() throws InitializationException {
        // TODO: the agent ID should not be hardcoded
        List<MBeanServer> servers = MBeanServerFactory.findMBeanServer("WebSphere");
        if (servers.isEmpty()) {
            mbs = ManagementFactory.getPlatformMBeanServer();
        } else {
            mbs = servers.get(0);
        }
        try {
            ObjectInstance inst = mbs.registerMBean(this, new ObjectName("com.googlecode.arit:type=" + LeakDetector.class.getSimpleName()));
            registeredObjectName = inst.getObjectName();
        } catch (JMException ex) {
            throw new InitializationException("Unable to register MBean", ex);
        }
        timer = new Timer("LeakDetectorTimer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runDetection();
            }
        }, 0, 60000);
    }
    
    private void runDetection() {
        for (Module module : reportGenerator.generateReport().getRootModules()) {
            if (module.getStatus() == ModuleStatus.STOPPED) {
                if (reportedModules.add(module.getId())) {
                    sendNotification(new Notification(LEAK_DETECTED, this, notificationSequence++,
                            "Resource leak detected in application " + module.getName()));
                }
            }
        }
    }
    
    public void dispose() {
        if (timer != null) {
            timer.cancel();
        }
        if (mbs != null && registeredObjectName != null) {
            try {
                mbs.unregisterMBean(registeredObjectName);
            } catch (JMException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}