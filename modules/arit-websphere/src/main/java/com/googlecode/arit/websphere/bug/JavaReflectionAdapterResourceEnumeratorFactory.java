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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.github.veithen.rbeans.RBeanFactory;
import com.github.veithen.rbeans.RBeanFactoryException;
import com.googlecode.arit.Logger;
import com.googlecode.arit.resource.ResourceEnumeratorFactory;
import com.googlecode.arit.resource.ResourceType;

public class JavaReflectionAdapterResourceEnumeratorFactory implements ResourceEnumeratorFactory<JavaReflectionAdapterResourceEnumerator> {
    private final RBeanFactory rbf;
    private final JavaReflectionAdapterStaticRBean rbean;
    
    @Autowired
    @Qualifier("websphere-bug")
    private ResourceType resourceType;
    
    public JavaReflectionAdapterResourceEnumeratorFactory() {
        RBeanFactory rbf;
        try {
            rbf = new RBeanFactory(JavaReflectionAdapterStaticRBean.class, JavaReflectionAdapterRBean.class);
        } catch (RBeanFactoryException ex) {
            rbf = null;
        }
        this.rbf = rbf;
        this.rbean = rbf == null ? null : rbf.createRBean(JavaReflectionAdapterStaticRBean.class);
    }

    public String getDescription() {
        return "Cached JavaReflectionAdapter instances (JR40617)";
    }

    public boolean isAvailable() {
        return rbean != null;
    }

    public JavaReflectionAdapterResourceEnumerator createEnumerator(Logger logger) {
        return new JavaReflectionAdapterResourceEnumerator(resourceType, rbf, rbean.getAdapters());
    }
}
