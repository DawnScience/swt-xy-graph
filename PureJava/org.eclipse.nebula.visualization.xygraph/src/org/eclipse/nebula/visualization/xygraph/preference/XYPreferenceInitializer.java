package org.eclipse.nebula.visualization.xygraph.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.nebula.visualization.xygraph.Activator;

public class XYPreferenceInitializer extends AbstractPreferenceInitializer {

	public XYPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setDefault(XYPreferences.TICKS_PROVIDER, XYPreferences.TICKS_PROVIDER_MARK_2);
	}
}
