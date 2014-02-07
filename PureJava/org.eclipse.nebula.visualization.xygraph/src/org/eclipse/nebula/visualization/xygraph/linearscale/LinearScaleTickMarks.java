package org.eclipse.nebula.visualization.xygraph.linearscale;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.nebula.visualization.xygraph.linearscale.AbstractScale.LabelSide;
import org.eclipse.nebula.visualization.xygraph.util.SWTConstants;

/**
 * Linear scale tick marks.
 * @author Xihui Chen
 */
public class LinearScaleTickMarks extends Figure {   



	/** the scale */
    private LinearScale scale;

    /** the line width */
    protected static final int LINE_WIDTH = 1;

    /** the tick length */
    public static final int MAJOR_TICK_LENGTH = 6;
    /** the tick length */
    public static final int MINOR_TICK_LENGTH = 3;

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
    public LinearScaleTickMarks(LinearScale scale) {
        
        this.scale = scale;

        setForegroundColor(scale.getForegroundColor());
    }

   protected void paintClientArea(Graphics graphics) {
	   graphics.translate(bounds.x, bounds.y);
	   ITicksProvider ticks = scale.getTicksProvider();

	   Dimension d = getSize();
        int width = d.width;
        int height = d.height;

        try {
        	graphics.pushState();
        	graphics.setAlpha(100);
        	if (scale.isHorizontal()) {
        		drawXTickMarks(graphics, ticks, scale.getTickLabelSide(), width, height);
        	} else {
        		drawYTickMarks(graphics, ticks, scale.getTickLabelSide(), width, height);
        	}
        } finally {
        	graphics.popState();
        }
  };

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
    private void drawXTickMarks(Graphics gc, ITicksProvider ticks,
            LabelSide tickLabelSide, int width, int height) {
    	
        // draw tick marks
        gc.setLineStyle(SWTConstants.LINE_SOLID);
        int imax = ticks.getMajorCount();
        if(scale.isLogScaleEnabled()) {
            int y;
        	for (int i = 0; i < imax; i++) {
                int x = ticks.getPosition(i);
                int tickLength =0;
                if(ticks.isVisible(i))
                	tickLength = MAJOR_TICK_LENGTH;
                else
                	tickLength = MINOR_TICK_LENGTH;
                y = tickLabelSide == LabelSide.Primary ? 0 : height - 1 - LINE_WIDTH - tickLength;;

                //draw minor ticks for log scale
                if(ticks.isVisible(i) || scale.isMinorTicksVisible())
                	gc.drawLine(x, y, x, y + tickLength);
        	}

			//draw minor ticks for log scale
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
            //draw minor ticks for linear scale
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

        //draw scale line
        if(scale.isScaleLineVisible()) {
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
    private void drawYTickMarks(Graphics gc, ITicksProvider ticks, LabelSide tickLabelSide, int width, int height) {
        // draw tick marks
        gc.setLineStyle(SWTConstants.LINE_SOLID);
        int y = 0;
        int imax = ticks.getMajorCount();
        if(scale.isLogScaleEnabled()) {
            int x;
        	for (int i = 0; i < imax; i++) {
                int tickLength =0;
                if(ticks.isVisible(i))
                	tickLength = MAJOR_TICK_LENGTH;
                else
                 	tickLength = MINOR_TICK_LENGTH;            

                x = tickLabelSide == LabelSide.Primary ? width - 1 - LINE_WIDTH - tickLength : LINE_WIDTH;
                y = height - ticks.getPosition(i);
                if(ticks.isVisible(i) || scale.isMinorTicksVisible())
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
