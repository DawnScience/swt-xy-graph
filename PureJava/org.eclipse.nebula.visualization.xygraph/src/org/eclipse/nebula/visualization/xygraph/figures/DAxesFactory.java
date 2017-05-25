/*******************************************************************************
 * Copyright (c) 2017, Diamond Light Source and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.eclipse.nebula.visualization.xygraph.figures;

import org.eclipse.nebula.visualization.xygraph.linearscale.AbstractScale.LabelSide;
import org.eclipse.nebula.visualization.xygraph.linearscale.LinearScale.Orientation;

/**
 * {@link IAxesFactory} that produces Diamond Light Source alternative {@link DAxis}
 *
 * @author Baha El-Kassaby - initial implementation
 *
 */
public class DAxesFactory implements IAxesFactory {

	@Override
	public Axis createXAxis() {
		DAxis newAxis = new DAxis(IXYGraph.X_AXIS, false);
		newAxis.setOrientation(Orientation.HORIZONTAL);
		newAxis.setTickLabelSide(LabelSide.Primary);
		return newAxis;
	}

	@Override
	public Axis createYAxis() {
		DAxis newAxis = new DAxis(IXYGraph.Y_AXIS, true);
		newAxis.setOrientation(Orientation.VERTICAL);
		newAxis.setTickLabelSide(LabelSide.Primary);
		newAxis.setAutoScaleThreshold(0.1);
		return newAxis;
	}
}