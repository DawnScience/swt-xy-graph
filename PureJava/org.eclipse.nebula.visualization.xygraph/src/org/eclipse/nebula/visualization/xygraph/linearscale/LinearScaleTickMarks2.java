/*******************************************************************************
 * Copyright (c) 2017 Diamond Light Source and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.eclipse.nebula.visualization.xygraph.linearscale;

import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.nebula.visualization.xygraph.linearscale.AbstractScale.LabelSide;
import org.eclipse.nebula.visualization.xygraph.util.SWTConstants;

/**
 * Linear scale tick marks 2. Diamond Light Source implementation for drawing X
 * and Y tick marks.
 * 
 * @author Baha El-Kassaby/Peter Chang - Diamond light Source contributions
 **/
public class LinearScaleTickMarks2 extends LinearScaleTickMarks {

	/**
	 * Constructor.
	 * 
	 * @param chart
	 *            the chart
	 * @param style
	 *            the style
	 * @param scale
	 *            the scale
	 */
	public LinearScaleTickMarks2(LinearScale scale) {
		super(scale);
	}

	/**
	 * Draw the X tick marks.
	 * 
	 * @param tickLabelPositions
	 *            the tick label positions
	 * @param tickLabelSide
	 *            the side of tick label relative to tick marks
	 * @param width
	 *            the width to draw tick marks
	 * @param height
	 *            the height to draw tick marks
	 * @param gc
	 *            the graphics context
	 */
	@Override
	protected void drawXTickMarks(Graphics gc, List<Integer> tickLabelPositions, LabelSide tickLabelSide, int width,
			int height) {
		// draw tick marks
		gc.setLineStyle(SWTConstants.LINE_SOLID);
		ITicksProvider ticks = scale.getTicksProvider();
		int imax = ticks.getMajorCount();
		if (scale.isLogScaleEnabled()) {
			int y;
			for (int i = 0; i < imax; i++) {
				int x = ticks.getPosition(i);
				int tickLength = ticks.isVisible(i) ? MAJOR_TICK_LENGTH : MINOR_TICK_LENGTH;
				y = tickLabelSide == LabelSide.Primary ? 0 : height - 1 - LINE_WIDTH - tickLength;

				// draw minor ticks for log scale
				if (ticks.isVisible(i) || scale.isMinorTicksVisible())
					gc.drawLine(x, y, x, y + tickLength);
			}

			// draw minor ticks for log scale
			if (scale.isMinorTicksVisible()) {
				final int start = scale.getTicksProvider().getHeadMargin();
				y = tickLabelSide == LabelSide.Primary ? 0 : height - 1 - LINE_WIDTH - MINOR_TICK_LENGTH;
				int jmax = ticks.getMinorCount();
				for (int j = 0; j < jmax; j++) {
					int x = ticks.getMinorPosition(j);
					if (x >= start && x < width)
						gc.drawLine(x, y, x, y + MINOR_TICK_LENGTH);
				}
			}
		} else {
			int y = tickLabelSide == LabelSide.Primary ? 0 : height - 1 - LINE_WIDTH - MAJOR_TICK_LENGTH;
			for (int i = 0; i < imax; i++) {
				int x = ticks.getPosition(i);
				gc.drawLine(x, y, x, y + MAJOR_TICK_LENGTH);
			}

			// draw minor ticks for linear scale
			if (scale.isMinorTicksVisible()) {
				final int start = scale.getTicksProvider().getHeadMargin();
				if (tickLabelSide == LabelSide.Secondary) {
					y = height - 1 - LINE_WIDTH - MINOR_TICK_LENGTH;
				}
				int jmax = ticks.getMinorCount();
				for (int j = 0; j < jmax; j++) {
					int x = ticks.getMinorPosition(j);
					if (x >= start && x < width)
						gc.drawLine(x, y, x, y + MINOR_TICK_LENGTH);
				}
			}
		}

		// draw scale line
		if (scale.isScaleLineVisible()) {
			if (tickLabelSide == LabelSide.Primary) {
				gc.drawLine(scale.getMargin(), 0, width - scale.getMargin(), 0);
			} else {
				gc.drawLine(scale.getMargin(), height - 1, width - scale.getMargin(), height - 1);
			}
		}
	}

	/**
	 * Draw the Y tick marks.
	 * 
	 * @param tickLabelPositions
	 *            the tick label positions
	 * @param tickLabelSide
	 *            the side of tick label relative to tick marks
	 * @param width
	 *            the width to draw tick marks
	 * @param height
	 *            the height to draw tick marks
	 * @param gc
	 *            the graphics context
	 */
	protected void drawYTickMarks(Graphics gc, List<Integer> tickLabelPositions, LabelSide tickLabelSide, int width,
			int height) {
		// draw tick marks
		gc.setLineStyle(SWTConstants.LINE_SOLID);
		int y = 0;
		ITicksProvider ticks = scale.getTicksProvider();
		int imax = ticks.getMajorCount();
		if (scale.isLogScaleEnabled()) {
			int x;
			for (int i = 0; i < imax; i++) {
				int tickLength = ticks.isVisible(i) ? MAJOR_TICK_LENGTH : MINOR_TICK_LENGTH;
				x = tickLabelSide == LabelSide.Primary ? width - 1 - LINE_WIDTH - tickLength : LINE_WIDTH;
				y = height - ticks.getPosition(i);
				if (ticks.isVisible(i) || scale.isMinorTicksVisible())
					gc.drawLine(x, y, x + tickLength, y);
			}

			// draw minor ticks for log scale
			if (scale.isMinorTicksVisible()) {
				final int end = height - scale.getTicksProvider().getTailMargin();
				x = tickLabelSide == LabelSide.Primary ? width - LINE_WIDTH - MINOR_TICK_LENGTH : LINE_WIDTH;
				final int jmax = ticks.getMinorCount();
				for (int j = 0; j < jmax; j++) {
					y = height - ticks.getMinorPosition(j);
					if (y >= 0 && y < end)
						gc.drawLine(x, y, x + MINOR_TICK_LENGTH, y);
				}
			}
		} else {
			int x = tickLabelSide == LabelSide.Primary ? width - LINE_WIDTH - MAJOR_TICK_LENGTH : LINE_WIDTH;
			for (int i = 0; i < imax; i++) {
				y = height - ticks.getPosition(i);
				gc.drawLine(x, y, x + MAJOR_TICK_LENGTH, y);
			}

			// draw minor ticks for linear scale
			if (scale.isMinorTicksVisible()) {
				final int end = height - scale.getTicksProvider().getTailMargin();
				if (tickLabelSide == LabelSide.Primary) {
					x = width - LINE_WIDTH - MINOR_TICK_LENGTH;
				}
				final int jmax = ticks.getMinorCount();
				for (int j = 0; j < jmax; j++) {
					y = height - ticks.getMinorPosition(j);
					if (y >= 0 && y < end)
						gc.drawLine(x, y, x + MINOR_TICK_LENGTH, y);
				}
			}
		}

		// draw scale line
		if (scale.isScaleLineVisible()) {
			if (tickLabelSide == LabelSide.Primary) {
				gc.drawLine(width - 1, scale.getMargin(), width - 1, height - scale.getMargin());
			} else {
				gc.drawLine(0, scale.getMargin(), 0, height - scale.getMargin());
			}
		}
	}
}
