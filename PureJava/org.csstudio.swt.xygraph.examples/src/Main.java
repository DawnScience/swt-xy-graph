import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {

	public static void main(String[] args) {
		final Shell shell = new Shell();
		shell.setText("Examples");
		FillLayout layout = new FillLayout(SWT.VERTICAL);
		layout.marginHeight=5;
		layout.marginWidth=5;
		layout.spacing=5;
		shell.setLayout(layout);
		
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Bar Chart Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BarChartExample.main(null);
			}
		});		
		
		button = new Button(shell, SWT.PUSH);
		button.setText("Simple XYGraph Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SimpleToolbarArmedXYGraphExample.main(null);
			}
		});		
		
		button = new Button(shell, SWT.PUSH);
		button.setText("Comprehensive XYGraph Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ComprehensiveExample.main(null);
			}
		});	
		
		
		button = new Button(shell, SWT.PUSH);
		button.setText("Intensity Graph Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IntensityGraphExample.main(null);
			}
		});
		
		button = new Button(shell, SWT.PUSH);
		button.setText("Gauge Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GaugeExample.main(null);
			}
		});
		
		
		button = new Button(shell, SWT.PUSH);
		button.setText("Knob Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				KnobExample.main(null);
			}
		});
		
		button = new Button(shell, SWT.PUSH);
		button.setText("Meter Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MeterExample.main(null);
			}
		});
		button = new Button(shell, SWT.PUSH);
		button.setText("Scaled Slider Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ScaledSliderExample.main(null);
			}
		});
		
		button = new Button(shell, SWT.PUSH);
		button.setText("Progress Bar Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProgressBarExample.main(null);
			}
		});
		
		
		button = new Button(shell, SWT.PUSH);
		button.setText("Thermometer Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ThermometerExample.main(null);
			}
		});
		
		button = new Button(shell, SWT.PUSH);
		button.setText("Tank Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TankExample.main(null);
			}
		});
		
		button = new Button(shell, SWT.PUSH);
		button.setText("Multiple Widgets Example");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MultipleWidgetsExample.main(null);
			}
		});
		
		
		shell.open();
		shell.pack();
		Display display = Display.getDefault();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

}
