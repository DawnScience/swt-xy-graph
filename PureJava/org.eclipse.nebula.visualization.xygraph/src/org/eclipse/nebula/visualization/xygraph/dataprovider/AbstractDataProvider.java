/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.eclipse.nebula.visualization.xygraph.dataprovider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.nebula.visualization.xygraph.linearscale.Range;

/**
 * This gives the most common implementation of the {@link IDataProvider} interface.
 * 
 * @author Xihui Chen
 * @author Kay Kasemir (synchronization)
 */
public abstract class AbstractDataProvider implements IDataProvider{


	protected boolean chronological = false;
	
	protected List<IDataProviderListener> listeners; 

	protected Range xDataMinMax = null;
	protected Range yDataMinMax = null;
	
	/**
	 * @param trace the trace which the data provider will provide data to.
	 * @param chronological true if the data is sorted chronologically on xAxis, 
	 * which means the data is sorted on X Axis.
	 */
	public AbstractDataProvider(boolean chronological) {
		this.chronological = chronological;
		listeners = new ArrayList<IDataProviderListener>();
	}
	

	
	/* (non-Javadoc)
	 * @see org.csstudio.sns.widgets.figureparts.IDataProvider#getSize()
	 */
	public abstract int getSize();
	
	/* (non-Javadoc)
	 * @see org.csstudio.sns.widgets.figureparts.IDataProvider#getSample(int)
	 */
	public abstract ISample getSample(int index);
	
	/**
	 * the update needed when {@link #fireDataChange()} was called.
	 */
	protected abstract void innerUpdate();
	
	/**
	 * update xDataMinMax and yDataMinMax whenever data changed.
	 * @param positiveOnly
	 */
	protected abstract void updateDataRange(boolean positiveOnly);

	/* (non-Javadoc)
	 * @see org.csstudio.sns.widgets.figureparts.IDataProvider#getXDataMinMax()
	 */
	@Override
	public Range getXDataMinMax(){
		return getXDataMinMax(false);
	}

	/* (non-Javadoc)
	 * @see org.csstudio.sns.widgets.figureparts.IDataProvider#getYDataMinMax()
	 */
	@Override
	public Range getYDataMinMax(){
		return getYDataMinMax(false);
	}

	/* (non-Javadoc)
	 * @see org.csstudio.sns.widgets.figureparts.IDataProvider#getXDataMinMax(boolean)
	 */
	@Override
	synchronized public Range getXDataMinMax(boolean positiveOnly) {		
		if(getSize() <=0)
			return null;
		updateDataRange(positiveOnly);
		return xDataMinMax;
	}
	
	/* (non-Javadoc)
	 * @see org.csstudio.sns.widgets.figureparts.IDataProvider#getYDataMinMax(boolean)
	 */
	@Override
	synchronized public Range getYDataMinMax(boolean positiveOnly) {
		if(getSize() <=0)
			return null;
		updateDataRange(positiveOnly);
		return yDataMinMax;
	}
	
	/**
	 * @param chronological the chronological to set
	 */
	public void setChronological(boolean chronological) {
		this.chronological = chronological;
	}

	/* (non-Javadoc)
	 * @see org.csstudio.sns.widgets.figureparts.IDataProvider#isChronological()
	 */
	public boolean isChronological() {
		return chronological;
	}
	
	/* (non-Javadoc)
	 * @see org.csstudio.sns.widgets.figureparts.IDataProvider#addDataProviderListener(org.csstudio.sns.widgets.figureparts.IDataProviderListener)
	 */
	public void addDataProviderListener(final IDataProviderListener listener){
		if(listeners.contains(listener))
			return;
		listeners.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see org.csstudio.sns.widgets.figureparts.IDataProvider#removeDataProviderListener(org.csstudio.sns.widgets.figureparts.IDataProviderListener)
	 */
	public boolean removeDataProviderListener(final IDataProviderListener listener){
		return listeners.remove(listener);
	}
	
	protected void fireDataChange(){
		innerUpdate();
		for(IDataProviderListener listener : listeners){
			listener.dataChanged(this);
		}
	}
}
