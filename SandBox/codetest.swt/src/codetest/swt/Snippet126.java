package codetest.swt;
 
/*
 * Table example snippet: place arbitrary controls in a table
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.internal.decorators.FullTextDecoratorRunnable;

public class Snippet126 {
public static void main(String[] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	final Table table = new Table (shell, SWT.BORDER | SWT.MULTI |SWT.HIDE_SELECTION);
	final Button bt = new Button(shell, SWT.PUSH);
	bt.addSelectionListener(new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			table.getColumn(0).dispose();
			table.redraw();
		}
	});
	table.setLinesVisible (true);
	for (int i=0; i<3; i++) {
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setWidth (100);
	}
	for (int i=0; i<5; i++) {
		new TableItem (table, SWT.NONE);
	}
	final TableItem [] items = table.getItems ();
	for (int i=0; i<items.length; i++) {
		TableEditor editor = new TableEditor (table);
		CCombo combo = new CCombo (table, SWT.NONE);
		combo.setText("CCombo");
		combo.add("item 1");
		combo.add("item 2");
		editor.grabHorizontal = true;
		editor.setEditor(combo, items[i], 0);
		editor = new TableEditor (table);
		final Text text = new Text (table, SWT.NONE);
		text.setText(items[i].getText(1));
		final TableItem itemT = items[i];
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				itemT.setText(1, text.getText());
			}
		});
		editor.grabHorizontal = true;
		editor.setEditor(text, items[i], 1);
		editor = new TableEditor (table);
		final Button button = new Button (table, SWT.CHECK);
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				for (int i=0; i<items.length; i++){
					System.out.println(items[i].getText(1));
					text.setVisible(button.getSelection());
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		button.pack ();
		editor.minimumWidth = button.getSize ().x;
		editor.horizontalAlignment = SWT.LEFT;
		editor.setEditor (button, items[i], 2);
	}
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}