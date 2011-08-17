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
package com.googlecode.arit.jcl;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import com.googlecode.arit.ResourceEnumerator;
import com.googlecode.arit.ResourceEnumeratorFactory;
import com.googlecode.arit.ResourceType;
import com.googlecode.arit.rbeans.RBeanFactory;
import com.googlecode.arit.rbeans.RBeanFactoryException;

@Component(role=ResourceEnumeratorFactory.class, hint="jcl")
public class LogFactoryResourceEnumeratorFactory implements ResourceEnumeratorFactory {
    private final LogFactoryRBean rbean;
    
    @Requirement(hint="jcl-factory")
    private ResourceType resourceType;
    
    public LogFactoryResourceEnumeratorFactory() {
        LogFactoryRBean rbean;
        try {
            rbean = new RBeanFactory(LogFactoryRBean.class).createRBean(LogFactoryRBean.class);
        } catch (RBeanFactoryException ex) {
            rbean = null;
        }
        this.rbean = rbean;
    }

    public String getDescription() {
        return "JCL LogFactory instances cached by the container";
    }

    public boolean isAvailable() {
        return rbean != null;
    }

    public ResourceEnumerator createEnumerator() {
        return new LogFactoryResourceEnumerator(resourceType, rbean.getFactories());
    }
}