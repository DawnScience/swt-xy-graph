package org.eclipse.nebula.visualization.xygraph.util;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.nebula.visualization.xygraph.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public PreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setDefault(Preferences.TICKS_PROVIDER, Preferences.TICKS_PROVIDER_MARK_2);
	}
}
