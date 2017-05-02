/*******************************************************************************
 * Copyright (c) 2017 Diamond Light Source and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.eclipse.nebula.visualization.xygraph.figures;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.nebula.visualization.xygraph.linearscale.ITicksProvider;
import org.eclipse.nebula.visualization.xygraph.linearscale.LinearScaleTickLabels;
import org.eclipse.nebula.visualization.xygraph.linearscale.LinearScaleTickLabels2;
import org.eclipse.nebula.visualization.xygraph.linearscale.LinearScaleTickMarks;
import org.eclipse.nebula.visualization.xygraph.linearscale.LinearScaleTickMarks2;
import org.eclipse.nebula.visualization.xygraph.linearscale.Range;
import org.eclipse.swt.widgets.Display;

/**
 * The Diamond Light Source implementation of the axis figure.
 *
 * @author Baha El-Kassaby - Diamond Light Source contributions
 */
public class DAxis extends Axis {

	/** if true, then ticks are based on axis dataset indexes */
	private boolean ticksIndexBased;

	private Map<Integer, Format> cachedFormats = new HashMap<Integer, Format>();

	private boolean ticksAtEnds = true;

	private boolean forceRange;

	/** the user format */
	protected boolean userDefinedFormat = false;

	private boolean axisAutoscaleTight = false;

	/**
	 * Constructor
	 *
	 * @param title
	 *            title of the axis
	 * @param yAxis
	 *            true if this is the Y-Axis, false if this is the X-Axis.
	 */
	public DAxis(final String title, final boolean yAxis) {
		super(title, yAxis);
	}

	@Override
	protected LinearScaleTickLabels createLinearScaleTickLabels() {
		return new LinearScaleTickLabels2(this);
	}

	@Override
	protected LinearScaleTickMarks createLinearScaleTickMarks() {
		return new LinearScaleTickMarks2(this);
	}

	/**
	 * Calculate span of a textual form of object in scale's orientation
	 *
	 * @param obj
	 *            object
	 * @return span in pixel
	 */
	public int calculateSpan(Object obj) {
		final Dimension extent = getDimension(obj);
		if (isHorizontal()) {
			return extent.width;
		}
		return extent.height;
	}

	@Override
	public int getMargin() {
		if (isDirty())
			setMargin(getTicksProvider().getHeadMargin());
		return getMargin(false);
	}

	/**
	 * Get scaling for axis in terms of pixels/unit
	 *
	 * @return scaling
	 */
	public double getScaling() {
		if (isLogScaleEnabled())
			return (Math.log10(max) - Math.log10(min)) / (getLength() - 2 * getMargin());
		return (max - min) / (getLength() - 2 * getMargin());
	}

	@Override
	protected void layout() {
		super.layout();
		layoutTicks();
	}

	protected void layoutTicks() {
		updateTick();
		Rectangle area = getClientArea();
		if (isHorizontal()) {
			if (getTickLabelSide() == LabelSide.Primary) {
				getScaleTickLabels().setBounds(
						new Rectangle(area.x, area.y + LinearScaleTickMarks.MAJOR_TICK_LENGTH + SPACE_BTW_MARK_LABEL,
								area.width, area.height - LinearScaleTickMarks.MAJOR_TICK_LENGTH));
				getScaleTickMarks().setBounds(area);
			} else {
				getScaleTickLabels().setBounds(new Rectangle(area.x,
						area.y + area.height - LinearScaleTickMarks.MAJOR_TICK_LENGTH
								- getScaleTickLabels().getTickLabelMaxHeight() - SPACE_BTW_MARK_LABEL,
						area.width, getScaleTickLabels().getTickLabelMaxHeight()));
				getScaleTickMarks().setBounds(new Rectangle(area.x, area.y + area.height - LinearScaleTickMarks.MAJOR_TICK_LENGTH,
						area.width, LinearScaleTickMarks.MAJOR_TICK_LENGTH));
			}
		} else {
			if (getTickLabelSide() == LabelSide.Primary) {
				getScaleTickLabels().setBounds(new Rectangle(
						area.x + area.width - LinearScaleTickMarks.MAJOR_TICK_LENGTH
								- getScaleTickLabels().getTickLabelMaxLength() - SPACE_BTW_MARK_LABEL,
						area.y, getScaleTickLabels().getTickLabelMaxLength(), area.height));
				getScaleTickMarks().setBounds(new Rectangle(
						area.x + area.width - LinearScaleTickMarks.MAJOR_TICK_LENGTH - LinearScaleTickMarks.LINE_WIDTH,
						area.y, LinearScaleTickMarks.MAJOR_TICK_LENGTH + LinearScaleTickMarks.LINE_WIDTH, area.height));
			} else {
				getScaleTickLabels().setBounds(new Rectangle(area.x + LinearScaleTickMarks.MAJOR_TICK_LENGTH + SPACE_BTW_MARK_LABEL,
								area.y, getScaleTickLabels().getTickLabelMaxLength(), area.height));
				getScaleTickMarks().setBounds(new Rectangle(area.x, area.y, LinearScaleTickMarks.MAJOR_TICK_LENGTH, area.height));
			}
		}
	}

	/**
	 * @param isTicksIndexBased
	 *            if true, make ticks based on axis dataset indexes
	 */
	public void setTicksIndexBased(boolean isTicksIndexBased) {
		if (ticksIndexBased != isTicksIndexBased)
			((LinearScaleTickLabels2)getScaleTickLabels()).setTicksIndexBased(isTicksIndexBased);
		ticksIndexBased = isTicksIndexBased;
	}

	/**
	 *
	 * @return True if ticks are index based
	 */
	public boolean isTicksIndexBased() {
		return ticksIndexBased;
	}

	@Override
	public String format(Object obj) {
		return format(obj, 0);
	}

	@Override
	public void updateTick() {
		if (isDirty()) {
			setLength(isHorizontal() ? getClientArea().width : getClientArea().height);
			if (getLength() > 2 * getMargin(false)) {
				Range r = getScaleTickLabels().update(getLength() - 2 * getMargin(false));
				if (r != null && !r.equals(getRange()) && !forceRange) {
					setLocalRange(r);
				} else {
					setLocalRange(null);
				}
			}
			setDirty(false);
		}
	}

	/**
	 * Formats the given object as a DateFormat if Date is enabled or as a
	 * DecimalFormat. This is based on an internal format pattern given the
	 * object in parameter. When formatting a date, if minOrMaxDate is true as
	 * well as autoFormat, then the SimpleDateFormat us used to format the
	 * object.
	 *
	 * @param obj
	 *            the object
	 * @param extraDP
	 *            must be non-negative
	 * @return the formatted string
	 */
	public String format(Object obj, int extraDP) {
		if (extraDP < 0) {
			throw new IllegalArgumentException("Number of extra decimal places must be non-negative");
		}
		if (cachedFormats.get(extraDP) == null) {
			if (isDateEnabled()) {
				if (isAutoFormat() || getFormatPattern() == null || getFormatPattern().equals("")
						|| getFormatPattern().equals(default_decimal_format)
						|| getFormatPattern().equals(DEFAULT_ENGINEERING_FORMAT)) {
					// (?) overridden anyway
					setFormatPattern(DEFAULT_DATE_FORMAT);
					double length = Math.abs(max - min);
					// less than a second
					if (length <= 1000 || getTimeUnit() == Calendar.MILLISECOND) {
						setFormatPattern("HH:mm:ss.SSS");
					}
					// less than a hour
					else if (length <= 3600000d || getTimeUnit() == Calendar.SECOND) {
						setFormatPattern("HH:mm:ss");
					}
					// less than a day
					else if (length <= 86400000d || getTimeUnit() == Calendar.MINUTE) {
						setFormatPattern("HH:mm");
					}
					// less than a week
					else if (length <= 604800000d || getTimeUnit() == Calendar.HOUR_OF_DAY) {
						setFormatPattern("dd HH:mm");
					}
					// less than a month
					else if (length <= 2592000000d || getTimeUnit() == Calendar.DATE) {
						setFormatPattern("MMMMM d");
					}
					// less than a year
					else if (length <= 31536000000d || getTimeUnit() == Calendar.MONTH) {
						setFormatPattern("yyyy MMMMM");
					} else {// if (timeUnit == Calendar.YEAR) {
						setFormatPattern("yyyy");
					}
					if (getFormatPattern() == null || getFormatPattern().equals("")) {
						setAutoFormat(true);
					}
				}
				cachedFormats.put(extraDP, new SimpleDateFormat(getFormatPattern()));
			} else {
				if (getFormatPattern() == null || getFormatPattern().isEmpty() || getFormatPattern().equals(default_decimal_format)
						|| getFormatPattern().equals(DEFAULT_DATE_FORMAT)) {
					setFormatPattern(getAutoFormat(min, max));
					if (getFormatPattern() == null || getFormatPattern().equals("")) {
						setAutoFormat(true);
					}
				}

				String ePattern = getFormatPattern();
				if (extraDP > 0) {
					int e = getFormatPattern().lastIndexOf('E');
					StringBuilder temp = new StringBuilder(e == -1 ? getFormatPattern() : getFormatPattern().substring(0, e));
					for (int i = 0; i < extraDP; i++) {
						temp.append('#');
					}
					if (e != -1) {
						temp.append(getFormatPattern().substring(e));
					}
					ePattern = temp.toString();
				}
				cachedFormats.put(extraDP, new DecimalFormat(ePattern));
			}
		}

		if (isDateEnabled() && obj instanceof Number) {
			return cachedFormats.get(extraDP).format(new Date(((Number) obj).longValue()));
		}
		return cachedFormats.get(extraDP).format(obj);
	}

	protected String getAutoFormat(double min, double max) {
		ITicksProvider ticks = getTicksProvider();
		if (ticks == null) {
			if ((max != 0 && Math.abs(Math.log10(Math.abs(max))) >= ENGINEERING_LIMIT)
					|| (min != 0 && Math.abs(Math.log10(Math.abs(min))) >= ENGINEERING_LIMIT)) {
				return DEFAULT_ENGINEERING_FORMAT;
			}
			return default_decimal_format;
		}
		return ticks.getDefaultFormatPattern(min, max);
	}

	@Override
	public void setDateEnabled(boolean dateEnabled) {
		super.setDateEnabled(dateEnabled);
		cachedFormats.clear();
		setDirty(true);
		revalidate();
	}

	@Override
	public void setFormatPattern(String formatPattern) {
		this.userDefinedFormat = true;
		setFormat(formatPattern);
	}

	private void setFormat(String formatPattern) {
		cachedFormats.clear();
		super.setFormatPattern(formatPattern);
	}

	@Override
	public void setRange(double lower, double upper) {
		Range old_range = getRange();
		if (old_range.getLower() == lower && old_range.getUpper() == upper) {
			return;
		}
		setTicksAtEnds(false);
		super.setRange(lower, upper);
		fireAxisRangeChanged(old_range, getRange());
	}

	@Override
	public void setAutoFormat(boolean autoFormat) {
		super.setAutoFormat(autoFormat);
		if (autoFormat) {
			cachedFormats.clear();
		}
	}

	@Override
	public void setLogScale(boolean enabled) throws IllegalStateException {
		boolean cur = isLogScaleEnabled();
		super.setLogScale(enabled);
		if (cur != enabled && getXyGraph() != null) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					getXyGraph().performAutoScale();
					getXyGraph().getPlotArea().layout();
					getXyGraph().revalidate();
					getXyGraph().repaint();
				}
			});
		}
		setTicksAtEnds(true);
	}

	@Override
	public boolean performAutoScale(boolean force) {
		// Anything to do? Autoscale not enabled nor forced?
		if (getTraceList().size() <= 0 || !(force || getAutoScale())) {
			return false;
		}

		// Get range of data in all traces
		Range range = getTraceDataRange();
		if (range == null) {
			return false;
		}

		double dataMin = range.getLower();
		double dataMax = range.getUpper();

		// Get current axis range, determine how 'different' they are
		double axisMax = getRange().getUpper();
		double axisMin = getRange().getLower();

		if (rangeIsUnchanged(dataMin, dataMax, axisMin, axisMax) || Double.isInfinite(dataMin)
				|| Double.isInfinite(dataMax) || Double.isNaN(dataMin) || Double.isNaN(dataMax)) {
			return false;
		}

		// The threshold is 'shared' between upper and lower range, times by 0.5
		final double thr = (axisMax - axisMin) * 0.5 * getAutoScaleThreshold();

		boolean lowerChanged = (dataMin - axisMin) < 0 || (dataMin - axisMin) >= thr;
		boolean upperChanged = (axisMax - dataMax) < 0 || (axisMax - dataMax) >= thr;
		// If both the changes are lower than threshold, return
		if (!lowerChanged && !upperChanged) {
			return false;
		}

		// Calculate updated range
		double newMax = upperChanged ? dataMax : axisMax;
		double newMin = lowerChanged ? dataMin : axisMin;
		range = !isInverted() ? new Range(newMin, newMax) : new Range(newMax, newMin);

		// by-pass overridden method as it sets ticks to false
		super.setRange(range.getLower(), range.getUpper());
		fireAxisRangeChanged(getRange(), range);
		setTicksAtEnds(!axisAutoscaleTight);
		repaint();
		return true;
	}

	/**
	 * Determines if upper or lower data has changed from current axis limits
	 *
	 * @param dataMin
	 *            - min of data in buffer
	 * @param dataMax
	 *            - max of data in buffer
	 * @param axisMin
	 *            - current axis min
	 * @param axisMax
	 *            - current axis max
	 * @return TRUE if data and axis max and min values are equal
	 */
	private boolean rangeIsUnchanged(double dataMin, double dataMax, double axisMin, double axisMax) {
		return Double.doubleToLongBits(dataMin) == Double.doubleToLongBits(axisMin)
				&& Double.doubleToLongBits(dataMax) == Double.doubleToLongBits(axisMax);
	}

	/**
	 * 
	 */
	public void clear() {
		for (Iterator<IAxisListener> it = listeners.iterator(); it.hasNext();) {
			if (getTraceList().contains(it.next()))
				it.remove();
		}
		getTraceList().clear();
	}

	/**
	 * @param set
	 *            whether autoscale sets axis range tight to the data or the end
	 *            of axis is set to the nearest tickmark
	 */
	public void setAxisAutoscaleTight(boolean axisTight) {
		this.axisAutoscaleTight = axisTight;
	}

	/**
	 * @return true if autoscaling axis is tight to displayed data
	 */
	public boolean isAxisAutoscaleTight() {
		return this.axisAutoscaleTight;
	}

	/**
	 * Sets whether ticks at ends of axis are shown
	 *
	 * @param ticksAtEnds
	 */
	public void setTicksAtEnds(boolean ticksAtEnds) {
		this.ticksAtEnds = ticksAtEnds;
	}

	/**
	 * Returns true if ticks at end of axis are shown
	 */
	public boolean hasTicksAtEnds() {
		return ticksAtEnds;
	}

	/**
	 * Sets whether there is a user defined format or not
	 *
	 * @param hasUserDefinedFormat
	 */
	public void setHasUserDefinedFormat(boolean hasUserDefinedFormat) {
		userDefinedFormat = hasUserDefinedFormat;
	}

	/**
	 *
	 * @return true if user format is defined
	 */
	public boolean hasUserDefinedFormat() {
		return userDefinedFormat;
	}
}
