package org.eclipse.nebula.visualization.xygraph.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public abstract class SingleSourceHelper {

	private static final SingleSourceHelper IMPL;
	
	static {
		IMPL = (SingleSourceHelper)ImplementationLoader.newInstance(SingleSourceHelper.class);
	}
	
	public static Cursor createCursor(
			Display display, ImageData imageData, int width, int height, int backUpSWTCursorStyle){
		return IMPL.createInternalCursor(display, imageData, width, height, backUpSWTCursorStyle);
	}
	
	public static Image createVerticalTextImage(String text, Font font, RGB color, boolean upToDown){
		return IMPL.createInternalVerticalTextImage(text, font, color, upToDown);
	}
	
	public static Image getXYGraphSnapShot(XYGraph xyGraph){
		return IMPL.getInternalXYGraphSnapShot(xyGraph);
	}
	
	public static String getImageSavePath(){
		return IMPL.getInternalImageSavePath(null);
	}
	public static String getImageSavePath(String[] filter){
		return IMPL.getInternalImageSavePath(filter);
	}
	public static IFile getProjectSaveFileLocation(String name){
		return IMPL.getProjectSaveFilePath(name);
	}
	

	protected abstract String getInternalImageSavePath(final String[] filterExtensions);
	
	protected abstract IFile getProjectSaveFilePath(String name);

	protected abstract Cursor createInternalCursor(
			Display display, ImageData imageData, int width, int height,int backUpSWTCursorStyle);
	
	protected abstract Image createInternalVerticalTextImage(
			String text, Font font, RGB color, boolean upToDown);
	
	protected abstract Image getInternalXYGraphSnapShot(XYGraph xyGraph);
	
	
}
