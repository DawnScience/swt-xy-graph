/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.eclipse.nebula.visualization.xygraph.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.Graphics;
import org.eclipse.nebula.visualization.xygraph.figures.IXYGraph;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Replacement for {@link SingleSourceHelper}
 * 
 * @author Baha El-Kassaby
 *
 */
public abstract class SingleSourceHelper2 {

	private static final SingleSourceHelper2 IMPL;

	static {
		IMPL = (SingleSourceHelper2) ImplementationLoader.newInstance(SingleSourceHelper2.class);
	}

	/**
	 * @param display
	 * @param imageData
	 * @param width
	 * @param height
	 * @param backUpSWTCursorStyle
	 * @return a cursor. The cursor will be automatically disposed when display
	 *         disposed, so please don't dispose it externally.
	 */
	public static Cursor createCursor(Display display, ImageData imageData, int width, int height,
			int backUpSWTCursorStyle) {
		return IMPL.createInternalCursor(display, imageData, width, height, backUpSWTCursorStyle);
	}

	public static Image createVerticalTextImage(String text, Font font, RGB color, boolean upToDown) {
		return IMPL.createInternalVerticalTextImage(text, font, color, upToDown);
	}

	public static Image getXYGraphSnapShot(IXYGraph xyGraph) {
		return IMPL.getInternalXYGraphSnapShot(xyGraph);
	}

	public static String getImageSavePath() {
		return IMPL.getInternalImageSavePath(null);
	}

	public static String getImageSavePath(String[] filter) {
		return IMPL.getInternalImageSavePath(filter);
	}

	public static IFile getProjectSaveFileLocation(String name) {
		return IMPL.getProjectSaveFilePath(name);
	}

	public static void setLineStyle_LINE_SOLID(Graphics graphics) {
		IMPL.internalSetLineStyle_LINE_SOLID(graphics);
	}

	protected abstract String getInternalImageSavePath(final String[] filterExtensions);

	protected abstract IFile getProjectSaveFilePath(String name);

	protected abstract Cursor createInternalCursor(Display display, ImageData imageData, int width, int height,
			int backUpSWTCursorStyle);

	protected abstract Image createInternalVerticalTextImage(String text, Font font, RGB color, boolean upToDown);

	protected abstract Image getInternalXYGraphSnapShot(IXYGraph xyGraph);

	protected abstract void internalSetLineStyle_LINE_SOLID(Graphics graphics);
}
