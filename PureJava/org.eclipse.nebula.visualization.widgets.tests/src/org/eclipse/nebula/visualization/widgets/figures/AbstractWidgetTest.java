/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.eclipse.nebula.visualization.widgets.figures;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.nebula.visualization.internal.widgets.introspection.Introspectable;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Test;

/**
 * The basement test class for all widgets to simplify the widget test.
 * 
 * @author Xihui Chen
 * 
 */
public abstract class AbstractWidgetTest {

	private int pdIndex = 0;
	private int runIndex = 1;
	private Figure testWidget;
	private Shell shell;
	private Text text;

	@Test
	public void testAll() throws Exception {
		shell = new Shell();

		shell.open();
		shell.setLayout(new GridLayout(1, false));
		final Canvas canvas = new Canvas(shell, SWT.None);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text = new Text(shell, SWT.READ_ONLY);
		text.setFont(XYGraphMediaFactory.getInstance().getFont("default", 18,
				SWT.BOLD));
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		final LightweightSystem lws = new LightweightSystem(canvas);
		lws.setContents(getTestBench());
		shell.setSize(800, 500);
		testGetBeanInfo();
		testWidget();
	}

	public void testGetBeanInfo() throws Exception {
		if (getWidgetInstance() instanceof Introspectable) {
			final PropertyDescriptor[] pds = ((Introspectable) getWidgetInstance())
					.getBeanInfo().getPropertyDescriptors();
			final List<String> propNameList = new ArrayList<String>();
			for (final PropertyDescriptor pd : pds) {
				System.out.println(pd.getName());
				propNameList.add(pd.getName());
			}
			final String[] predefinedPropNames = getPropertyNames();
			for (final String p : predefinedPropNames) {
				if (!propNameList.contains(p)) {
					throw new Exception(
							"The widget doesn't have the property: " + p);
				}
			}
			final List<String> prePropList = Arrays.asList(predefinedPropNames);
			for (final String p : propNameList) {
				if (!prePropList.contains(p)) {
					System.err.println(p + " is not predefined in test.");
				}
			}
		} else {
			throw new Exception("The widget under test is not introspectable");
		}
	}

	/**
	 * Graphically test widget.
	 * 
	 * @throws Exception
	 */
	public void testWidget() throws Exception {

		final Figure widget = getWidgetInstance();

		shell.setText("Widget Figure Test");
		final Display display = Display.getCurrent();

		if (widget instanceof Introspectable) {
			final BeanInfo bi = ((Introspectable) widget).getBeanInfo();
			final PropertyDescriptor[] pds = bi.getPropertyDescriptors();

			final Runnable propertyTestTask = new Runnable() {

				public void run() {
					int nextRunTime = 10;

					if ((getTestRepeatCount() > 0)
							&& (pdIndex > getTestRepeatCount() * pds.length)) {
						shell.close();
						shell.dispose();	
						display.dispose();
						return;
					}
					final PropertyDescriptor pd = pds[(pdIndex) % pds.length];
					if (runIndex % getRepeatCountOnEachProperty() == 0) {
						pdIndex++;
					}
					final Method writeMethod = pd.getWriteMethod();
					if (writeMethod != null) {
						final Object testData = generateTestData(pd, runIndex);
						if (testData != null) {
							try {
								text.setText(pd.getName() + " : " + testData);
								System.out.println(pd.getName() + " : "
										+ testData);
								writeMethod.invoke(widget, testData);
								nextRunTime = getAutoTestSpeedInterval();
							} catch (final Exception e) {
								e.printStackTrace();
							}
						}
					}
					runIndex++;
					display.timerExec(nextRunTime, this);
				}
			};
			if (isAutoTest()) {
				display.asyncExec(propertyTestTask);
			}

		} else {
			throw new Exception("The widget under test is not introspectable");
		}
		while (!shell.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @return the count of the tests need to be repeated. -1 for infinite test.
	 *         Test can be terminated at any time by closing the shell.
	 */
	public int getTestRepeatCount() {
		return 2;
	}

	/**
	 * @return the predefined properties of the widget.
	 */
	public String[] getPropertyNames() {
		return new String[] { "backgroundColor", "border", "bounds", "cursor",
				"enabled", "font", "foregroundColor", "location", "opaque",
				"size", "visible"};
	}

	/**
	 * @return the widget under test.
	 */
	public Figure getWidgetInstance() {
		if (testWidget == null) {
			testWidget = createTestWidget();
		}
		return testWidget;
	}

	/**
	 * @return create the widget to be tested.
	 */
	public abstract Figure createTestWidget();

	/**
	 * Generate test data.
	 * 
	 * @param pd
	 *            the property descriptor of the property under test.
	 * @param seed
	 *            the seed that could be used as a reference in generating test
	 *            data. It is the runIndex integer number by default.
	 * @return
	 */
	public Object generateTestData(final PropertyDescriptor pd,
			final Object seed) {
		final int REPEAT_COUNT = getRepeatCountOnEachProperty();
		final Class<?> propType = pd.getPropertyType();
		if (propType == boolean.class) {
			if ((seed != null) && (seed instanceof Integer)) {
				return ((Integer) seed) % 2 == 0;
			}
			return Math.random() > 0.5;

		} else if (propType == Color.class) {
			return XYGraphMediaFactory.getInstance().getColor(
					new RGB((int) (Math.random() * 255),
							(int) (Math.random() * 255),
							(int) (Math.random() * 255)));
		} else if (propType == double.class) {
			if ((seed != null) && (seed instanceof Integer)) {
				if (((Integer) seed) % REPEAT_COUNT == 1) {
					return Double.NaN;
				} else if (((Integer) seed) % REPEAT_COUNT == 2) {
					return Double.NEGATIVE_INFINITY;
				} else if (((Integer) seed) % REPEAT_COUNT == 3) {
					return Double.POSITIVE_INFINITY;
				} else if (((Integer) seed) % REPEAT_COUNT == 4) {
					return Double.MAX_VALUE;
				} else if (((Integer) seed) % REPEAT_COUNT == 5) {
					return Double.MIN_VALUE;
				} else if (((Integer) seed) % REPEAT_COUNT == 5) {
					return 0;
				} else {
					return Math.random() * 100;
				}
			}
			return Math.random() * 100;

		} else if (propType == int.class) {
			if (seed != null && seed instanceof Integer) {
				if (((Integer) seed) % REPEAT_COUNT == 1)
					return Integer.MAX_VALUE;
				else if (((Integer) seed) % REPEAT_COUNT == 2)
					return Integer.MIN_VALUE;
				else if (((Integer) seed) % REPEAT_COUNT == 3)
					return 0;
				else
					return (int) (Math.random() * 100);
			}
			return (int) (Math.random() * 100);

		} else if (propType == String.class) {
			return "Hello, I'm " + seed;
		} else if (propType == Font.class) {
			if (seed != null && seed instanceof Integer) {
				return XYGraphMediaFactory.getInstance().getFont("Arial",
						(Integer) seed % 100, (Integer) seed % 3);
			}
		}
		return null;

	}

	protected int getRepeatCountOnEachProperty() {
		return 8;
	}

	protected int getAutoTestSpeedInterval() {
		return 50;
	}

	/**
	 * @return the test bench figure on which the widget will placed on.
	 */
	private Figure getTestBench() {
		final Figure result = new TestBench();
		result.setLayoutManager(new StackLayout());
		return result;
	}

	/**
	 * @return true if the test will automatically poll on every property.
	 */
	public boolean isAutoTest() {
		return true;
	}

	public String[] concatenateStringArrays(final String[] A, final String[] B) {
		final String[] C = new String[A.length + B.length];
		System.arraycopy(A, 0, C, 0, A.length);
		System.arraycopy(B, 0, C, A.length, B.length);
		return C;
	}

	class TestBench extends Figure {
		public TestBench() {
			add(getWidgetInstance());
		}
	}

}
