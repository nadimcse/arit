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
package com.googlecode.arit.websphere.jaxws;

import com.googlecode.arit.axis2.AxisServiceRBean;
import com.googlecode.arit.rbeans.Accessor;
import com.googlecode.arit.rbeans.RBean;
import com.googlecode.arit.rbeans.Target;

@Target("com.ibm.ws.wsaddressing.jaxws.urimap.EndpointData")
public interface EndpointDataRBean extends RBean {
    @Accessor(name="axisService")
    AxisServiceRBean getAxisService();
}