import java.awt.Color;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

public class ColorPaletteGenerator {
    private static final float[][] sbValues = { { 0.8f, 0.8f }, { 0.8f, 0.5f }, { 0.5f, 0.8f } }; 
    
    /**
     * Generate a color palette with the specified minimum size. The method
     * works with the HSB color space and will generate a sequence of colors
     * distributed uniformly over the hue component. For larger palette sizes,
     * it will also start using different saturation/brightness combinations.
     * 
     * @param minSize
     *            the minimum size of the palette
     * @return the generated color palette
     */
    public static Color[] createColorPalette(int minSize) {
        int hueSteps = 4;
        int sbCombinations = 1;
        int size;
        while (true) {
            size = hueSteps * sbCombinations;
            if (size >= minSize) {
                break;
            } else if (hueSteps > sbCombinations*5 && sbCombinations < sbValues.length) {
                sbCombinations++;
            } else {    
                hueSteps = hueSteps + 2 + hueSteps/3;
            }
        }
        
        Color[] colors = new Color[size];
        int index = 0;
        for (int i=0; i<hueSteps; i++) {
            for (int j=0; j<sbCombinations; j++) {
                colors[index++] = Color.getHSBColor((float)i/(float)hueSteps, sbValues[j][0], sbValues[j][1]);
            }
        }
        
        return colors;
    }
    
    public static <T> Map<T,Color> assignColors(Collection<T> collection) {
        Map<T,Color> result = new HashMap<T,Color>();
        Color[] palette = createColorPalette(collection.size());
        int paletteSize = palette.length;
        for (T item : collection) {
            int index = item.hashCode() % paletteSize;
            Color color;
            while (true) {
                color = palette[index];
                if (color != null) {
                    break;
                } else {
                    index = (index+1) % paletteSize;
                }
            }
            palette[index] = null;
            result.put(item, color);
        }
        return result;
    }
    
    public static void main(String[] args) throws Exception {
        Color[] colors = createColorPalette(100);
        
        PrintWriter out = new PrintWriter(new FileOutputStream("test.html"));
        out.println("<table>");
        for (Color color : colors) {
            out.print("<tr><td style='background-color: #");
            out.print(Integer.toHexString((color.getRGB() & 0xffffff) | 0x1000000).substring(1));
            out.println("'>TEST</td></tr>");
        }
        out.println("</table>");
        out.close();
    }
}
