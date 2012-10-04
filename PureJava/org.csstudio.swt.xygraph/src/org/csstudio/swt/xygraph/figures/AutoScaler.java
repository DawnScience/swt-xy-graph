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
import org.csstudio.swt.xygraph.util.Log10;

/**
 *
 */
public class AutoScaler implements IAutoScaler {

	@Override
	public Range calculateNewRange(Range inputRange, Range currentRange, boolean isLogScale, double threshold) {

		double tempMin = inputRange.getLower();
		double tempMax = inputRange.getUpper();

		// Get current axis range, determine how 'different' they are
		double min = currentRange.getLower();
		double max = currentRange.getUpper();

		if (isLogScale)
		{   // Transition into log space
		    tempMin = Log10.log10(tempMin);
		    tempMax = Log10.log10(tempMax);
		    max = Log10.log10(max);
		    min = Log10.log10(min);
		}

		final double thr = (max - min)*threshold;
		final double cor = (tempMax - tempMin)*threshold;

		//if both the changes are lower than threshold, return
		if(((tempMin - min)>=0 && (tempMin - min)<thr)
				&& ((max - tempMax)>=0 && (max - tempMax)<thr)){
			return null;
		}else { //expand more space than needed
			if ((tempMin - min)<0)  tempMin -= cor;		
			if ((tempMax - max)>0)  tempMax += cor;
		}

		// Any change at all?
		if((Double.doubleToLongBits(tempMin) == Double.doubleToLongBits(min)
				&& Double.doubleToLongBits(tempMax) == Double.doubleToLongBits(max)) ||
				Double.isInfinite(tempMin) || Double.isInfinite(tempMax) ||
				Double.isNaN(tempMin) || Double.isNaN(tempMax))
			return null;

        if (isLogScale)
        {   // Revert from log space 
            tempMin = Log10.pow10(tempMin);
            tempMax = Log10.pow10(tempMax);
        }

        return new Range(tempMin, tempMax);
	}

}
