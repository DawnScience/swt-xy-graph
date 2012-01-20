package codetest.jogl;

import javax.media.opengl.GLProfile;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "codetest.jogl"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	// NOTE for Linux: Needed to set up threading properly for JOGL. This
	// has to be done before any X11 calls, so it goes here in the class
	// Eclipse loads first. If you don't put this early enough, you may
	// get SIGSEGV in libpthread.so (or other sorts of multithreading errors)
	// when you try to run the program.
	// NOTE for Mac OS X: If you don't set -Djava.awt.headless=true in the
	// program's VM arguments, this call may freeze the program. This is because
	// it tries to load the AWT library, which can't coexist with SWT on Mac OS X.
	static {
	    GLProfile.initSingleton( false );
	}

	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
