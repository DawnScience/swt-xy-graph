package codetest.swt;

/*
 * Table example snippet: update table item text
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class SpreadSheetTableTest {

static char content = 'a';
public static void main(String[] args) {
	final Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10, 10, 200, 240);
	shell.setLayout(new FillLayout());
	final SpreadSheetTable textTable = new SpreadSheetTable(shell, SWT.None);
	
	textTable.setColumnsCount(4);

	Button button = new Button(shell, SWT.PUSH);
	button.setText("Change");
	button.pack();
//	button.setLocation(10, 180);
	button.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
//			textTable.setColumnsCount(4);
			textTable.insertRow(0);
		}
	});

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}


}