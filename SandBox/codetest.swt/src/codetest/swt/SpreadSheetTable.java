package codetest.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**A table allow displaying and editing 2d text array as in spreadsheet.
 * The internal data operated by this table is a nested list.
 * @author Xihui Chen
 *
 */
/**
 * @author 5hz
 * 
 */
public class SpreadSheetTable extends Composite {
	
	public interface ITableEditingListener {
		/**Called when the value in a cell has been modified from cell editor.
		 * @param row index of the row
		 * @param col index of the column
		 * @param oldValue old value in the cell
		 * @param newValue new value in the cell.
		 */
		public void cellValueChanged(int row, int col, String oldValue, String newValue);
	}

	private class TextEditingSupport extends EditingSupport {

		private TableViewerColumn viewerColumn;
		private TextCellEditor textCellEditor;
		
		private String oldValue;
		private ViewerCell viewerCell;

		public TextEditingSupport(ColumnViewer viewer,
				TableViewerColumn viewerColumn) {
			super(viewer);
			this.viewerColumn = viewerColumn;
		}

		@Override
		protected boolean canEdit(Object element) {
			return editable;
		}
		
		@Override
		protected void initializeCellEditorValue(CellEditor cellEditor,
				ViewerCell cell) {
			this.viewerCell = cell;
			super.initializeCellEditorValue(cellEditor, cell);
			
		}

		private int findColumnIndex() {
			return viewerCell.getColumnIndex();
		}
		
		private int findRowIndex(){
			Table table = tableViewer.getTable();
			TableItem cellItem = (TableItem)viewerCell.getItem();
			return table.indexOf(cellItem);
//			int index = table.getTopIndex();
//			Rectangle clientArea = table.getClientArea();
//			
//			while (index < table.getItemCount()) {
//				boolean visible = false;
//				TableItem item = table.getItem(index);		
//				Rectangle rect = item.getBounds();
//				if(cellItem == item)
//					return index;
//				if (!visible && rect.intersects(clientArea)) {
//					visible = true;
//				}
//				if (!visible)
//					return -1;
//				index++;
//			}
//			return -1;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			if (textCellEditor == null)
				textCellEditor = new TextCellEditor(tableViewer.getTable());
			return textCellEditor;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object getValue(Object element) {
			oldValue = ((List<String>) element).get(findColumnIndex());
			return oldValue;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void setValue(Object element, Object value) {
			int col = findColumnIndex();
			String oldValue = ((List<String>) element).get(col);
			((List<String>) element).set(col, value.toString());
			if(!value.equals(oldValue)){
				if(!listeners.isEmpty()){
					int row = findRowIndex();
					for(Object listener: listeners.getListeners()){
						((ITableEditingListener)listener).cellValueChanged(row, col, oldValue, value.toString());
					}
				}
			}
			((TableItem)viewerCell.getItem()).setText(col, value.toString());
		}

	}

	private static class TextTableLableProvider extends LabelProvider implements
			ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public String getColumnText(Object element, int columnIndex) {
			return ((List<String>) element).get(columnIndex);
		}

	}

	private static final int DEFAULT_COLUMN_WIDTH = 50;

	private TableViewer tableViewer;

	private boolean editable = true;

	private List<List<String>> input;
	
	private ListenerList listeners;

	/**Create a spreadsheet table.
	 * @param parent parent composite.
	 * @param style the style of widget to construct
	 */
	public SpreadSheetTable(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout());
		tableViewer = new TableViewer(this, SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		setInput(new ArrayList<List<String>>());
		listeners = new ListenerList();
	}

	public void addTableEditingListener(ITableEditingListener listener){
		listeners.add(listener);
	}
	
	
	
	/**
	 * Auto size all columns.
	 */
	public void autoSizeColumns() {
		for (TableColumn column : tableViewer.getTable().getColumns()) {
			column.pack();
		}
	}

	/**
	 * Delete a column.
	 * 
	 * @param index
	 *            index of the column.
	 */
	public void deleteColumn(int index) {
		tableViewer.getTable().getColumn(index).dispose();
		for (int i = 0; i < getRowCount(); i++) {
			input.get(i).remove(index);
		}
	}

	/**
	 * Delete a row.
	 * 
	 * @param index
	 *            index of the row.
	 */
	public void deleteRow(int index) {
		input.remove(index);
		tableViewer.refresh();
	}

	/**
	 * Get text of a cell.
	 * 
	 * @param row
	 *            row index of the cell.
	 * @param col
	 *            column index of the cell.
	 * @return the cell text.
	 */
	public String getCellText(int row, int col) {
		return input.get(row).get(col);
	}

	/**
	 * Get number of columns.
	 * 
	 * @see {@link Table#getColumnCount()}
	 */
	public int getColumnCount() {
		return tableViewer.getTable().getColumnCount();
	}

	/**Get row and column index under given point.
	 * @param point the widget-relative coordinates
	 * @return int[0] is row index, int[1] is column index. 
	 * null if no cell was found under given point.
	 */
	public int[] getRowColumnIndex(Point point) {
		Table table = tableViewer.getTable();
		ViewerCell cell = tableViewer.getCell(point);
		if(cell == null)
			return null;
		int col = cell.getColumnIndex();
//		int row = table.indexOf((TableItem) cell.getItem());
//		return new int[]{row, col};
		int row = -1;
		// get row index
		Rectangle clientArea = table.getClientArea();

		int index = table.getTopIndex();
		while (index < table.getItemCount()) {
			boolean visible = false;
			TableItem item = table.getItem(index);
			Rectangle rect = item.getBounds(col);
			if (rect.contains(point)) {
				row = index;
				return new int[] { row, col };
			}
			if (!visible && rect.intersects(clientArea)) {
				visible = true;
			}

			if (!visible)
				return new int[] { row, col };
			index++;
		}
		return new int[] { row, col };
	}

	/**
	 * Get content of the table in a 2D string array.
	 * 
	 * @return content of the table.
	 */
	public String[][] getContent() {
		String[][] result = new String[input.size()][getColumnCount()];
		for (int i = 0; i < input.size(); i++) {
			for (int j = 0; j < getColumnCount(); j++) {
				result[i][j] = input.get(i).get(j);
			}
		}
		return result;
	}

	/**
	 * Get input of the table by which the table is backed. To keep the table's
	 * content synchronized with the table, client should call
	 * {@link #refresh()} if the returned list has been modified.
	 * 
	 * @return the input of the table.
	 */
	public List<List<String>> getInput() {
		return input;
	}

	public int getRowCount() {
		return input.size();
	}

	/**
	 * Get selected part.
	 * 
	 * @return the 2D string array under selection.
	 */
	@SuppressWarnings("unchecked")
	public String[][] getSelection() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		String[][] result = new String[selection.size()][getColumnCount()];
		int i = 0;
		for (Object o : selection.toArray()) {
			for (int j = 0; j < getColumnCount(); j++) {
				result[i][j] = ((List<String>) o).get(j);
			}
			i++;
		}
		return result;
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	/**
	 * Insert a column. Default values for the new column are empty strings.
	 * 
	 * @param index
	 *            index of the column.
	 */
	public void insertColumn(int index) {
		for (List<String> row : input) {
			row.add(index, ""); //$NON-NLS-1$
		}
		final TableViewerColumn viewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE, index);
		viewerColumn.getColumn().setMoveable(false);
		viewerColumn.getColumn().setWidth(DEFAULT_COLUMN_WIDTH);
		viewerColumn.setEditingSupport(new TextEditingSupport(tableViewer,
				viewerColumn));
		tableViewer.setLabelProvider(new TextTableLableProvider());
	}

	/**
	 * Insert a row. Default values for the new row are empty strings.
	 * 
	 * @param index
	 *            index of the row.
	 */
	public void insertRow(int index) {
		String[] array = new String[getColumnCount()];
		Arrays.fill(array, ""); //$NON-NLS-1$
		input.add(index, new ArrayList<String>(Arrays.asList(array)));
		tableViewer.refresh(false);
	}

	/**
	 * @return true if table is editable.
	 */
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void pack() {
		for (int i = 0; i < getColumnCount(); i++)
			tableViewer.getTable().getColumn(i).pack();
		super.pack();
	}

	public void refresh() {
		getTableViewer().refresh();
	}

	/**
	 * Set the text of a cell.
	 * 
	 * @param row
	 *            row index of the cell.
	 * @param col
	 *            column index of the cell.
	 * @param text
	 *            text to be set.
	 */
	public void setCellText(int row, int col, String text) {
		input.get(row).set(col, text);
		tableViewer.getTable().getItem(row).setText(col, text);
	}

	/**
	 * Set the header of a column.
	 * 
	 * @param columnIndex
	 *            index of the column.
	 * @param header
	 *            header text.
	 */
	public void setColumnHeader(int columnIndex, String header) {
		Assert.isLegal(columnIndex >= 0 && columnIndex < getColumnCount(),
				NLS.bind("Column index {0} doesn't exist!", columnIndex));
		tableViewer.getTable().getColumn(columnIndex).setText(header);
	}

	/**
	 * Set column headers. If the size of the headers array is larger than the
	 * existing columns count. It will increase the columns count automatically.
	 * 
	 * @param headers
	 *            headers text.
	 */
	public void setColumnHeaders(String[] headers) {
		if (headers.length > tableViewer.getTable().getColumnCount()) {
			setColumnsCount(headers.length);
		}
		for (int i = 0; i < headers.length; i++) {
			tableViewer.getTable().getColumn(i).setText(headers[i]);
		}
	}

	/**
	 * Set number of columns. If the new count is less than old count, columns
	 * from right will be deleted. If the new count is more than old count, new
	 * columns will be appended to the right.
	 * 
	 * @param count
	 *            number of columns.
	 */
	public void setColumnsCount(int count) {
		TableColumn[] columns = tableViewer.getTable().getColumns();
		int oldCount = columns.length;
		if (count == oldCount)
			return;
		if (count < oldCount) {
			for (int i = count; i < oldCount; i++) {
				columns[i].dispose();
			}
			for (List<String> row : input) {
				for (int i = oldCount - 1; i > count - 1; i--) {
					row.remove(i);
				}
			}
			return;
		}

		// if count > old count
		for (List<String> row : input) {
			for (int i = 0; i < count - oldCount; i++) {
				row.add("");
			}
		}

		for (int i = oldCount; i < count; i++) {
			final TableViewerColumn viewerColumn = new TableViewerColumn(
					tableViewer, SWT.NONE);
			viewerColumn.getColumn().setMoveable(false);
			viewerColumn.getColumn().setWidth(DEFAULT_COLUMN_WIDTH);
			viewerColumn.setEditingSupport(new TextEditingSupport(tableViewer,
					viewerColumn));
		}
		tableViewer.setLabelProvider(new TextTableLableProvider());

	}

	public void setColumnWidth(int col, int width) {
		tableViewer.getTable().getColumn(col).setWidth(width);
	}

	/**
	 * Set content of the table.Old content in table will be replaced by the new
	 * content.
	 * 
	 * @param content
	 *            the new content.
	 */
	public void setContent(String[][] content) {
		Assert.isNotNull(content);
		input.clear();
		if (content.length <= 0) {
			tableViewer.refresh();
			return;
		}
		for (int i = 0; i < content.length; i++) {
			List<String> row = new ArrayList<String>(content[0].length);
			for (int j = 0; j < content[0].length; j++) {
				row.add(content[i][j]);
			}
			input.add(row);

		}
		setColumnsCount(content[0].length);
		tableViewer.refresh();
	}

	/**
	 * Set if the table is editable.
	 * 
	 * @param editable
	 *            true if table is editable.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Show/hide table column headers.
	 * 
	 * @param show
	 *            the new visibility state
	 */
	public void setHeadersVisible(boolean show) {
		tableViewer.getTable().setHeaderVisible(show);
	}

	/**
	 * Set input of the table. The input is the back of the table, so content of
	 * the input is always synchronized with content of the table.
	 * 
	 * @param input
	 *            input of the table.
	 */
	public void setInput(List<List<String>> input) {
		tableViewer.setInput(input);
		this.input = input;
	}

}
