/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.eclipse.nebula.visualization.widgets.figures;
import org.eclipse.draw2d.Figure;
import org.eclipse.nebula.visualization.widgets.figures.GaugeFigure;

/**
 * @author Xihui Chen
 *
 */
public class GaugeTest extends AbstractRoundRampedWidgetTest{

	@Override
	public Figure createTestWidget() {
		return new GaugeFigure();
	}
	
	
	@Override
	public String[] getPropertyNames() {
		String[] superProps =  super.getPropertyNames();
		String[] myProps = new String[]{
				"effect3D",
				"needleColor"
		};
		
		return concatenateStringArrays(superProps, myProps);
	}
	
	@Override
	public boolean isAutoTest() {
		return true;
	}

		
}
