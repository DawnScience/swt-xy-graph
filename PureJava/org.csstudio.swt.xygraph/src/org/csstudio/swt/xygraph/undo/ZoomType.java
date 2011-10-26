/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.swt.xygraph.undo;

import org.csstudio.swt.xygraph.Messages;
import org.csstudio.swt.xygraph.figures.XYGraphFlags;
import org.csstudio.swt.xygraph.util.CustomMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**The type of zoom on XYGraph.
 * @author Xihui Chen
 * @author Kay Kasemir
 */
@SuppressWarnings("nls")
public enum ZoomType{
        /** Interactive Rubberband zoom */
        RUBBERBAND_ZOOM(Messages.Zoom_Rubberband,
		        CustomMediaFactory.getInstance().getImage("images/RubberbandZoom.png"),
		        CustomMediaFactory.getInstance().getImage("images//RubberbandZoomCursor.png"),
				XYGraphFlags.COMBINED_ZOOM | XYGraphFlags.SEPARATE_ZOOM),
				
		/** Zoom via 'cursors' for horizontal start/end position */		
		HORIZONTAL_ZOOM(Messages.Zoom_Horiz,
				CustomMediaFactory.getInstance().getImage("images/HorizontalZoom.png"),
				CustomMediaFactory.getInstance().getImage("images/HorizontalZoomCursor.png"),
                XYGraphFlags.COMBINED_ZOOM | XYGraphFlags.SEPARATE_ZOOM),
				
		/** Zoom via 'cursors' for vertical start/end position */     
		VERTICAL_ZOOM(Messages.Zoom_Vert,
				CustomMediaFactory.getInstance().getImage("images/VerticalZoom.png"),
				CustomMediaFactory.getInstance().getImage("images/VerticalZoomCursor.png"),
                XYGraphFlags.COMBINED_ZOOM | XYGraphFlags.SEPARATE_ZOOM),
				
		/** Zoom 'in' around mouse pointer */
		ZOOM_IN(Messages.Zoom_In,
				CustomMediaFactory.getInstance().getImage("images/ZoomIn.png"),
				CustomMediaFactory.getInstance().getImage("images/ZoomInCursor.png"),
                XYGraphFlags.COMBINED_ZOOM),

        /** Zoom 'out' around mouse pointer */
        ZOOM_OUT(Messages.Zoom_Out,
        		CustomMediaFactory.getInstance().getImage("images/ZoomOut.png"),
        		CustomMediaFactory.getInstance().getImage("images/ZoomOutCursor.png"),
                XYGraphFlags.COMBINED_ZOOM),

        /** Zoom 'in' around mouse pointer along horizontal axis */
        ZOOM_IN_HORIZONTALLY(Messages.Zoom_InHoriz,
        		CustomMediaFactory.getInstance().getImage("images/ZoomInHoriz.png"),
        		CustomMediaFactory.getInstance().getImage("images/ZoomInHorizCursor.png"),
                XYGraphFlags.SEPARATE_ZOOM),
				
        /** Zoom 'out' around mouse pointer along horizontal axis */
        ZOOM_OUT_HORIZONTALLY(Messages.Zoom_OutHoriz,
        		CustomMediaFactory.getInstance().getImage("images/ZoomOutHoriz.png"),
        		CustomMediaFactory.getInstance().getImage("images/ZoomOutHorizCursor.png"),
                XYGraphFlags.SEPARATE_ZOOM),

        /** Zoom 'in' around mouse pointer along vertical axis */
        ZOOM_IN_VERTICALLY(Messages.Zoom_InVert,
        		CustomMediaFactory.getInstance().getImage("images/ZoomInVert.png"),
        		CustomMediaFactory.getInstance().getImage("images/ZoomInVertCursor.png"),
                XYGraphFlags.SEPARATE_ZOOM),
				
        /** Zoom 'out' around mouse pointer along vertical axes */
        ZOOM_OUT_VERTICALLY(Messages.Zoom_OutVert,
        		CustomMediaFactory.getInstance().getImage("images/ZoomOutVert.png"),
        		CustomMediaFactory.getInstance().getImage("images/ZoomOutVertCursor.png"),
                XYGraphFlags.SEPARATE_ZOOM),
				
        /** Zoom 'out' around mouse pointer */
		PANNING(Messages.Zoom_Pan,
				CustomMediaFactory.getInstance().getImage("images/Panning.png"),
				CustomMediaFactory.getInstance().getImage("images/PanningCursor.png"),
                XYGraphFlags.COMBINED_ZOOM | XYGraphFlags.SEPARATE_ZOOM),
				
        /** Disarm zoom behavior */
		NONE(Messages.Zoom_None,
				CustomMediaFactory.getInstance().getImage("images/MouseArrow.png"), null,
                XYGraphFlags.COMBINED_ZOOM | XYGraphFlags.SEPARATE_ZOOM);
		
		final private Image iconImage;
		final private String description;
		final private Cursor cursor;
		final private int flags;
		
		/** Initialize
		 *  @param description Description used for tool tip
		 *  @param iconImage Button icon
		 *  @param cursorImage Cursor when zoom type is selected
         *  @param flags Bitwise 'or' of flags that specify in which zoom
         *               configurations this zoom type should be included
         *  @see XYGraphFlags#COMBINED_ZOOM
         *  @see XYGraphFlags#SEPARATE_ZOOM
		 */
		private ZoomType(final String description, 
				final Image iconImage, final Image cursorImage,
				final int flags){
			this.description = description;
			this.iconImage = iconImage;
			if(cursorImage == null)
				cursor = new Cursor(Display.getDefault(), SWT.CURSOR_ARROW);
			else
				cursor = new Cursor(Display.getDefault(), cursorImage.getImageData(), 8, 8);
			CustomMediaFactory.getInstance().registerCursor(cursor);
			this.flags = flags;
		}
		
		/**
		 * @return the iconImageData
		 */
		public Image getIconImage() {
			return iconImage;
		}
		
		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		
		/**
		 * @return the cursor
		 */
		public Cursor getCursor() {
			return cursor;
		}		

		/** Check if this zoom mode should be offered when a graph was
		 *  created with given flags
		 *  @param flags Flags of the XYGraph tool bar
		 *  @return <code>true</code> if this zoom type applies
		 */
		public boolean useWithFlags(final int flags)
		{
		    return (this.flags & flags) > 0;
		}
		
		
	
		
		@Override
		public String toString() {
			return description;
		}
	}
