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
package com.googlecode.arit.tomcat;

import java.util.List;

import com.googlecode.arit.ModuleDescription;
import com.googlecode.arit.ModuleInspector;
import com.googlecode.arit.ModuleStatus;
import com.googlecode.arit.ModuleType;
import com.googlecode.arit.rbeans.RBeanFactory;

public class TomcatModuleInspector implements ModuleInspector {
    private final RBeanFactory rbf;
    private final ModuleType warModuleType;

    public TomcatModuleInspector(RBeanFactory rbf, ModuleType warModuleType) {
        this.rbf = rbf;
        this.warModuleType = warModuleType;
    }

    public List<ModuleDescription> listModules() {
        return null;
    }

    public ModuleDescription inspect(ClassLoader classLoader) {
        if (rbf.getRBeanInfo(WebappClassLoaderRBean.class).getTargetClass().isInstance(classLoader)) {
            WebappClassLoaderRBean wacl = rbf.createRBean(WebappClassLoaderRBean.class, classLoader);
            ProxyDirContextRBean context = (ProxyDirContextRBean)wacl.getResources();
            // Tomcat removes the DirContext when stopping the application
            if (context == null) {
                return new ModuleDescription(warModuleType, null, classLoader, ModuleStatus.STOPPED);
            } else {
                return new ModuleDescription(warModuleType, context.getContextName(), classLoader, ModuleStatus.STARTED);
            }
        } else {
            return null;
        }
    }
}