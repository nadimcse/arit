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
package com.googlecode.arit.jce;

import java.security.Security;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import com.googlecode.arit.ResourceEnumerator;
import com.googlecode.arit.ResourceEnumeratorFactory;
import com.googlecode.arit.ResourceType;

@Component(role=ResourceEnumeratorFactory.class, hint="jce")
public class JceProviderEnumeratorFactory implements ResourceEnumeratorFactory {
    @Requirement(hint="jce")
    private ResourceType resourceType;
    
    public boolean isAvailable() {
        return true;
    }

    public String getDescription() {
        return "JCE providers";
    }

    public ResourceEnumerator createEnumerator() {
        return new JceProviderEnumerator(Security.getProviders(), resourceType);
    }
}