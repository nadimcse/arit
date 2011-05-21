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
package com.googlecode.arit.jul;

import java.util.logging.Handler;
import java.util.logging.Logger;

import org.codehaus.plexus.PlexusTestCase;

import com.googlecode.arit.ResourceEnumeratorFactory;

public class HandlerResourceEnumeratorTest extends PlexusTestCase {
    public void test() throws Exception {
        Handler handler = new NullHandler();
        Logger logger = Logger.getLogger("");
        logger.addHandler(handler);
        boolean found = false;
        try {
            HandlerResourceEnumerator enumerator = ((HandlerResourceEnumeratorFactory)lookup(ResourceEnumeratorFactory.class, "jul")).createEnumerator();
            while (enumerator.nextResource()) {
                if (enumerator.getHandler() == handler) {
                    found = true;
                    // Don't break the loop here so that we test that the loop terminates properly
                }
            }
        } finally {
            logger.removeHandler(handler);
        }
        assertTrue(found);
    }
}