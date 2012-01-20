package codetest.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class SWTTableWithRowHeader {
	public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new GridLayout());
	ScrolledComposite sc = new ScrolledComposite(shell, SWT.BORDER |
	SWT.H_SCROLL | SWT.V_SCROLL);
	sc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	Composite tableWrapper = new Composite(sc, SWT.NONE);
	tableWrapper.setLayout(new GridLayout(2, false));
	sc.setContent(tableWrapper);
	final Table rowHeaders = new Table(tableWrapper, SWT.NONE|SWT.MULTI);
	rowHeaders.setHeaderVisible(true);
	for (int i = 0; i < 100; i++) {
	TableItem item = new TableItem(rowHeaders, SWT.NONE);
	item.setText("row "+i);
	}
	final Table table = new Table(tableWrapper, SWT.FULL_SELECTION | SWT.MULTI);
	table.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
	table.setHeaderVisible(true);
	TableColumn column1 = new TableColumn(table, SWT.NONE);
	column1.setText("Column 1");
	TableColumn column2 = new TableColumn(table, SWT.NONE);
	column2.setText("Column 2");
	for (int i = 0; i < 100; i++) {
	TableItem item = new TableItem(table, SWT.NONE);
	item.setText(new String[] {"item "+i, "sadsada"});
	}
	column1.setWidth(300);
	column2.setWidth(300);
	sc.setExpandHorizontal(true);
	sc.setExpandVertical(true);
	sc.setMinSize(tableWrapper.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	rowHeaders.addSelectionListener(new SelectionAdapter() {
	public void widgetSelected(SelectionEvent e) {
	table.setSelection(rowHeaders.getSelectionIndices());
	}
	});
	table.addSelectionListener(new SelectionAdapter() {
	public void widgetSelected(SelectionEvent e) {
	rowHeaders.setSelection(table.getSelectionIndices());
	}
	});
	shell.open ();
	while (!shell.isDisposed ()) {
	if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
	}
}

