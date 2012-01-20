package codetest.jogl;


import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		// editor area invisible to avoid empty editor frame below our view
		layout.setEditorAreaVisible( false );
		layout.addView( "codetest.jogl.joglview", IPageLayout.TOP,
						IPageLayout.RATIO_MAX, IPageLayout.ID_EDITOR_AREA);
		}
}
