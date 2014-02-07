package org.eclipse.nebula.visualization.xygraph.figures;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

public class NoRepeatsPointsList extends PointList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8769260981259832495L;

	/**
	 * Does not add the same point twice in a row.
	 */
	public void addPoint(int x, int y) {
		
		if (size()<1) {
			super.addPoint(x, y);
			return;
		}
        final Point last = getPoint(size()-1);
        if (x==last.x && y==last.y) {
        	return;
        }
        // The Trace class produces slightly wobbly data because of rounding error.
        // We iron this out by ignoring values which are almost the same.
		if (size()>=3) {
			if (x<=last.x+1 && y<=last.y+1 && x>=last.x-1 && y>=last.y-1) {
		        final Point lastb1 = getPoint(size()-2);
		        if (x==lastb1.x && y==lastb1.y) {
		        	return;
		        }
			}
		}
		
		super.addPoint(x, y);
	}
}
