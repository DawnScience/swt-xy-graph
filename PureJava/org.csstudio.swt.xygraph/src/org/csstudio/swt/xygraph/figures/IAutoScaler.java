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

package org.csstudio.swt.xygraph.figures;

import org.csstudio.swt.xygraph.linearscale.Range;

/**
 * Interface for an automatic means to set a range
 */
public interface IAutoScaler {

	/**
	 * Calculate a range
	 * @param inputRange   (new) data range
	 * @param currentRange currently shown range
	 * @param isLogScale   true if range is needed for a log scale
	 * @param threshold    fraction of current range to use for determining whether to create a new range
	 * @return new range (or null for no change needed)
	 */
	public Range calculateNewRange(Range inputRange, Range currentRange, boolean isLogScale, double threshold);
}
