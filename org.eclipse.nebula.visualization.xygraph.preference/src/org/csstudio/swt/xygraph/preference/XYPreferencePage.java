package org.csstudio.swt.xygraph.preference;

import org.csstudio.swt.xygraph.Activator;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class XYPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Combo xyc;

	@Override
	protected Control createContents(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1, false));

		Group xyg = new Group(comp, SWT.NONE);
		xyg.setText("XY Graphing");
		xyg.setLayout(new GridLayout(2, false));
		xyg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		Label xyl = new Label(xyg, SWT.LEFT);
		xyl.setText("Tick provider");
		xyc = new Combo(xyg, SWT.RIGHT | SWT.READ_ONLY);
		xyc.add("Original");
		xyc.add("Mark 2");
		if (getPreferenceStore().isDefault(XYPreferences.TICKS_PROVIDER)
				|| XYPreferences.TICKS_PROVIDER_MARK_2.equals(getPreferenceStore().getString(XYPreferences.TICKS_PROVIDER)))
			xyc.select(1);
		else
			xyc.select(0);
		return comp;
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public boolean performOk() {
		getPreferenceStore().setValue(XYPreferences.TICKS_PROVIDER, xyc.getSelectionIndex() == 0 ? XYPreferences.TICKS_PROVIDER_ORIGINAL :
			XYPreferences.TICKS_PROVIDER_MARK_2);
		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		if (XYPreferences.TICKS_PROVIDER_MARK_2.equals(getPreferenceStore().getDefaultString(XYPreferences.TICKS_PROVIDER)))
			xyc.select(1);
		else
			xyc.select(0);

		super.performDefaults();
	}
}
