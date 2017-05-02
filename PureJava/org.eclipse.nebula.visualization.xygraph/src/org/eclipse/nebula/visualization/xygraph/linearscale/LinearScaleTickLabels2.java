/*******************************************************************************
 * Copyright (c) 2017 Diamond Light Source and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.eclipse.nebula.visualization.xygraph.linearscale;

import org.eclipse.draw2d.Graphics;
import org.eclipse.nebula.visualization.xygraph.figures.DAxis;

/**
 * Linear Scale tick labels used for the new implementation of {@link Axis} :
 * {@link DAxis}.
 *
 * @author Baha El-Kassaby, Peter Chang: Diamond Light Source contribution
 */
public class LinearScaleTickLabels2 extends LinearScaleTickLabels {

	/**
	 * Constructor. (Takes a @link DAxis) as parameter.
	 *
	 * @param linearScale
	 *            the DAxis scale
	 */
	public LinearScaleTickLabels2(DAxis linearScale) {
		super(linearScale);
		setTicksIndexBased(linearScale.isTicksIndexBased());
	}

	@Override
	protected void createLinearScaleTicks() {
		setTicksProvider(new LinearScaleTicks2((DAxis) getScale()));
	}

	@Override
	protected void drawXTick(Graphics graphics) {
		// draw tick labels
		final int imax = getTicksProvider().getMajorCount();
		for (int i = 0; i < imax; i++) {
			if (getTicksProvider().isVisible(i)) {
				graphics.drawText(getTicksProvider().getLabel(i), getTicksProvider().getLabelPosition(i), 0);
			}
		}
	}

	@Override
	protected void drawYTick(Graphics graphics) {
		// draw tick labels
		final int imax = getTicksProvider().getMajorCount();
		if (imax < 1)
			return;
		final boolean hasNegative = getTicksProvider().getLabel(0).startsWith(MINUS);
		final int minus = getScale().getDimension(MINUS).width;
		for (int i = 0; i < imax; i++) {
			if (getTicksProvider().isVisible(i)) {
				String text = getTicksProvider().getLabel(i);
				int x = (hasNegative && !text.startsWith(MINUS)) ? minus : 0;
				graphics.drawText(text, x, getTicksProvider().getLabelPosition(i));
			}
		}
	}

	/**
	 * @param isTicksIndexBased
	 *            if true, make ticks based on axis dataset indexes
	 */
	public void setTicksIndexBased(boolean isTicksIndexBased) {
		((LinearScaleTicks2) getTicksProvider()).setTicksIndexBased(isTicksIndexBased);
	}
}
