package org.eclipse.nebula.visualization.xygraph;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

public class DrawImageSWT {
	final static Display display = new Display();

	public static void main(String[] args) {
		int w = 350, h = 200;
		Image image = new Image(display, w, h);
		GC gc = new GC(image);
		gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
		gc.setLineWidth(10);
		gc.drawLine(0, 0, w, h);
		gc.drawLine(w, 0, 0, h);

		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] {image.getImageData()};
		loader.save("/tmp/gc.png", SWT.IMAGE_PNG);
		gc.dispose();
		image.dispose();
		display.dispose();
	}
}
