package codetest.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

/**A table allow displaying and editing 2d text array as in spreadsheet.
 * The internal data operated by this table is a nested list.
 * @author Xihui Chen
 *
 */
public class SpreadSheetTable extends Composite {

	private TableViewer tableViewer;
	
	private boolean editable = true;
	
	private List<List<String>> input;

	public SpreadSheetTable(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		tableViewer = new TableViewer(this, SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.BORDER | SWT.FULL_SELECTION |SWT.MULTI);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		setInput(new ArrayList<List<String>>());
			
	}
	
	/**
	 * @param input
	 */
	public void setInput(List<List<String>> input){
		tableViewer.setInput(input);
		this.input = input;
	}
	
	public void setColumnsCount(int count){
		TableColumn[] columns = tableViewer.getTable().getColumns();
		int oldCount = columns.length;
		for(TableColumn column : columns){
			column.dispose();
		}
		
		if(count>oldCount){
			for(List<String> row : input){
				for(int i=0; i<count-oldCount; i++){
					row.add("");
				}
			}
		}
		
		for(int i=0; i<count; i++){
			final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);		
			viewerColumn.getColumn().setText("PV Name");
			viewerColumn.getColumn().setMoveable(false);
			viewerColumn.getColumn().setWidth(220);		
			
			viewerColumn.getColumn().pack();
			
			viewerColumn.setEditingSupport(new TextEditingSupport(tableViewer, i));
		}
		tableViewer.setLabelProvider(new TextTableLableProvider());

	}
	
	public void insertRow(int index){		
		String[] array = new String[tableViewer.getTable().getColumns().length];
		Arrays.fill(array, "");
		input.add(index, Arrays.asList(array));
		tableViewer.refresh();
	}
	
	
	class TextTableLableProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public String getColumnText(Object element, int columnIndex) {
			return ((List<String>)element).get(columnIndex);
		}
		
	}
	
	class TextEditingSupport extends EditingSupport {

		private int columnIndex;
		public TextEditingSupport(ColumnViewer viewer, int columnIndex) {
			super(viewer);
			this.columnIndex = columnIndex;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(tableViewer.getTable());
		}

		@Override
		protected boolean canEdit(Object element) {
			return editable;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object getValue(Object element) {
			return ((List<String>)element).get(columnIndex);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void setValue(Object element, Object value) {
			((List<String>)element).set(columnIndex, value.toString());
			getViewer().refresh();
		}
		
	}

}
