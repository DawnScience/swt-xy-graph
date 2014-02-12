package org.eclipse.nebula.visualization.xygraph.linearscale;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.nebula.visualization.xygraph.preference.XYPreferences;

/**
 * Linear Scale tick labels.
 * @author Xihui Chen
 */
public class LinearScaleTickLabels extends Figure {

	private ITicksProvider ticks;

	private IScaleProvider scale;

	/**
	 * Constructor.
	 * 
	 * @param linearScale
	 *            the scale
	 */
	protected LinearScaleTickLabels(IScaleProvider linearScale) {
		scale = linearScale;
		ticks = TickFactory.createTicksProvider(scale, XYPreferences.TICKS_PROVIDER_ORIGINAL);

		setTicksIndexBased(scale.isTicksIndexBased());
		setFont(scale.getFont());
		setForegroundColor(scale.getForegroundColor());
	}

	public ITicksProvider getTicksProvider() {
		return ticks;
	}

    /**
     * Updates the tick labels.
     * 
     * @param length
     *            scale tick length (without margin)
     */
    protected Range update(int length) {
    	final Range range = scale.getScaleRange();
		return ticks.update(range.getLower(), range.getUpper(), length);
    }

    @Override
    protected void paintClientArea(Graphics graphics) {
    	graphics.translate(bounds.x, bounds.y);
    	graphics.setFont(getFont());
    	if (scale.isHorizontal()) {
            drawXTick(graphics);
        } else {
            drawYTick(graphics);
        }

    	super.paintClientArea(graphics);
    };

    /**
     * Draw the X tick.
     * 
     * @param graphics
     *            the graphics context
     */
    private void drawXTick(Graphics graphics) {
        // draw tick labels
        final int imax = ticks.getMajorCount();
        for (int i = 0; i < imax; i++) {
            if (ticks.isVisible(i)) {
                graphics.drawText(ticks.getLabel(i), ticks.getLabelPosition(i), 0);
            }
        }
    }

    private static final String MINUS = "-";

    /**
     * Draw the Y tick.
     * 
     * @param graphics
     *            the graphics context
     */
    private void drawYTick(Graphics graphics) {
        // draw tick labels
        final int imax = ticks.getMajorCount();
        if (imax < 1)
            return;
        final boolean hasNegative = ticks.getLabel(0).startsWith(MINUS);
        final int minus = scale.calculateDimension(MINUS).width;
        for (int i = 0; i < imax; i++) {
            if (ticks.isVisible(i)) {
                String text = ticks.getLabel(i);
                int x = (hasNegative && !text.startsWith(MINUS)) ? minus : 0;
                graphics.drawText(text, x, ticks.getLabelPosition(i));
            }
        }
    }

	/**
	 * @return the tickLabelMaxLength
	 */
	public int getTickLabelMaxLength() {
		return ticks.getMaxWidth();
	}

	/**
	 * @return the tickLabelMaxHeight
	 */
	public int getTickLabelMaxHeight() {
		return ticks.getMaxHeight();
	}

	public IScaleProvider getScale() {
		return scale;
	}

	public void setScale(IScaleProvider scale) {
		this.scale = scale;
	}

	/**
	 * @param isTicksIndexBased if true, make ticks based on axis dataset indexes
	 */
	public void setTicksIndexBased(boolean isTicksIndexBased) {
		if (ticks instanceof LinearScaleTicks2) {
			((LinearScaleTicks2) ticks).setTicksIndexBased(isTicksIndexBased);
		}
	}
}
