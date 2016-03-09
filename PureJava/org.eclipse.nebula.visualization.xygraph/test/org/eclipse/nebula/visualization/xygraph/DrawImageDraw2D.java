package org.eclipse.nebula.visualization.xygraph;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DrawImageDraw2D extends Figure {
	final static Display display = new Display();

	public static void main(String[] args) {
		int w = 350, h = 200;
		final Shell shell = new Shell(display);
		shell.setSize(w, h);
		shell.setLayout(new FillLayout());
		final FigureCanvas canvas = new FigureCanvas(shell);
		final DrawImageDraw2D f = new DrawImageDraw2D();
		canvas.setContents(f);
		canvas.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ESC) {
					f.saveImage(shell, canvas);
					shell.close();
				}
			}
		});

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void saveImage(Shell shell, Canvas canvas) {
		org.eclipse.swt.graphics.Rectangle bnds = shell.getBounds();
		Shell nShell = new Shell(display);
		nShell.setBounds(bnds);

		Image image = new Image(display, bnds.width, bnds.height);
		GC gc = new GC(image);
		GC fgc = new GC(canvas);
		gc.setBackground(fgc.getBackground());
		gc.setForeground(fgc.getForeground());
		gc.setAdvanced(fgc.getAdvanced());

		SWTGraphics g = new SWTGraphics(gc);
		setBounds(new Rectangle(0, 0, bnds.width, bnds.height));
//		layout();
		paint(g);

		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] {image.getImageData()};
		loader.save("/tmp/draw2d.png", SWT.IMAGE_PNG);

		g.dispose();
		gc.dispose();
		image.dispose();
	}

	@Override
	protected void paintFigure(Graphics g) {
		Rectangle bnds = getBounds();
		int x = bnds.x;
		int y = bnds.y;
		int w = bnds.width;
		int h = bnds.height;
		g.setForegroundColor(display.getSystemColor(SWT.COLOR_RED));
		g.setLineWidth(10);
		g.drawLine(x, y, x + w, y + h);
		g.drawLine(x + w, y, x, y + h);
	}
}
