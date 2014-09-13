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
package com.googlecode.arit.resource;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.googlecode.arit.icon.GeneratedIconProvider;

public final class ResourceType extends GeneratedIconProvider {
    private final Color color;
    private final String identifier;
    private final boolean showResourceId;
    
    public ResourceType(Color color, String identifier, boolean showResourceId) {
        this.color = color;
        this.identifier = identifier;
        this.showResourceId = showResourceId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isShowResourceId() {
        return showResourceId;
    }

    @Override
    protected void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillOval(4, 4, 8, 8);
    }
}
