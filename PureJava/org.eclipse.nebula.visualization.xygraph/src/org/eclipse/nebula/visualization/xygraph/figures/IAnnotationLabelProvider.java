/*******************************************************************************
 * Copyright (c) 2017 Diamond Light Source Ltd
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthew Gerring - initial commit and implementation
 ******************************************************************************/
package org.eclipse.nebula.visualization.xygraph.figures;

/**
 * Interface used to get the information text of a label
 *
 * @author Matthew Gerring
 *
 */
public interface IAnnotationLabelProvider {

	/**
	 * Return a string to be used on the annotation label.
	 * 
	 * @param xValue
	 * @param yValue
	 * @return null to use normal labelling, "" to have no label, or a string to
	 *         be the label.
	 */
	public String getInfoText(double xValue, double yValue, boolean showName, boolean showSample, boolean showPosition);

}
