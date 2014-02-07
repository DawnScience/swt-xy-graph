/*
 * Copyright 2012 Diamond Light Source Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.nebula.visualization.xygraph.linearscale;

import java.util.List;

/**
 * 
 *
 */
public interface ITicksProvider {

    /**
     * Gets the tick positions.
     * 
     * @return the tick positions
     */
    public List<Integer> getPositions();

    /**
     * @param index
     * @return tick position
     */
    public int getPosition(int index);

    /**
     * @param index
     * @return tick value
     */
    public double getValue(int index);

    /**
     * @param index
     * @return tick label
     */
    public String getLabel(int index);

    /**
     * @param index
     * @return label position
     */
    public int getLabelPosition(int index);

    /**
     * @param index
     * @return true if tick is visible
     */
    public boolean isVisible(int index);

    /**
     * @return number of major ticks
     */
    public int getMajorCount();

    /**
     * @param index
     * @return minor tick position
     */
    public int getMinorPosition(int index);

    /**
     * @return number of minor ticks
     */
    public int getMinorCount();

    /**
     * Update ticks
     * 
     * @param min
     * @param max
     * @param length
     * @return new axis range
     */
    public Range update(double min , double max, int length);

    /**
     * 
     * @return maximum width in pixels of tick labels
     */
    public int getMaxWidth();

    /**
     * 
     * @return maximum height in pixels of tick labels
     */
    public int getMaxHeight();

    /**
     * @param min
     * @param max
     * @return default format pattern for labels
     */
    public String getDefaultFormatPattern(double min , double max);

    /**
     * @return margin in pixel between edge of client area and head of axis line
     */
    public int getHeadMargin();

    /**
     * @return margin in pixel between edge of client area and tail of axis line
     */
    public int getTailMargin();
}
