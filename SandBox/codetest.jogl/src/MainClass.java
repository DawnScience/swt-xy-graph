import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainClass extends ApplicationWindow {
  public MainClass() {
    super(null);
  }

  public void run() {
    setBlockOnOpen(true);
    open();
    Display.getCurrent().dispose();
  }

  protected void configureShell(Shell shell) {
    super.configureShell(shell);
    shell.setText("Show Progress");
  }

  protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(1, true));

    final Button indeterminate = new Button(composite, SWT.CHECK);
    indeterminate.setText("Indeterminate");
    Button showProgress = new Button(composite, SWT.NONE);
    showProgress.setText("Show Progress");

    final Shell shell = parent.getShell();

    showProgress.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        try {
          new ProgressMonitorDialog(shell).run(true, true, new LongRunningOperation(indeterminate
              .getSelection()));
          System.out.println("finished");
        } catch (InvocationTargetException e) {
          MessageDialog.openError(shell, "Error", e.getMessage());
        } catch (InterruptedException e) {
          MessageDialog.openInformation(shell, "Cancelled", e.getMessage());
        }
      }
    });

    parent.pack();
    return composite;
  }

  public static void main(String[] args) {
    new MainClass().run();
  }
}

class LongRunningOperation implements IRunnableWithProgress {
  private static final int TOTAL_TIME = 10000;

  private static final int INCREMENT = 500;

  private boolean indeterminate;

  public LongRunningOperation(boolean indeterminate) {
    this.indeterminate = indeterminate;
  }

  public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
    monitor.beginTask("Running long running operation", indeterminate ? IProgressMonitor.UNKNOWN
        : TOTAL_TIME);
    for (int total = 0; total < TOTAL_TIME && !monitor.isCanceled(); total += INCREMENT) {
      Thread.sleep(INCREMENT);
      monitor.worked(INCREMENT);
      if (total == TOTAL_TIME / 2)
        monitor.subTask("Doing second half");
    }
    monitor.done();
    if (monitor.isCanceled())
      throw new InterruptedException("The long running operation was cancelled");
  }
}
     