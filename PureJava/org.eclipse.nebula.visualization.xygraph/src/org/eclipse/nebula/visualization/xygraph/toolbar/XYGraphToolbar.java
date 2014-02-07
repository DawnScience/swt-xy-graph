package org.eclipse.nebula.visualization.xygraph.toolbar;

import java.util.List;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ButtonGroup;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToggleButton;
import org.eclipse.draw2d.ToggleModel;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraphFlags;
import org.eclipse.nebula.visualization.xygraph.undo.AddAnnotationCommand;
import org.eclipse.nebula.visualization.xygraph.undo.IOperationsManagerListener;
import org.eclipse.nebula.visualization.xygraph.undo.OperationsManager;
import org.eclipse.nebula.visualization.xygraph.undo.RemoveAnnotationCommand;
import org.eclipse.nebula.visualization.xygraph.undo.ZoomType;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Display;


/**The toolbar for an xy-graph.
 * @author Xihui Chen
 * @author Kay Kasemir (some zoom operations)
 */
public class XYGraphToolbar extends Figure {
	
	
    private final static int BUTTON_SIZE = 25;

    final protected XYGraph xyGraph;
	
	final protected ButtonGroup zoomGroup;
	
	public XYGraphToolbar(final XYGraph xyGraph) {		
	    this(xyGraph, XYGraphFlags.COMBINED_ZOOM);
	}
	
	/** Initialize
	 *  @param xyGraph XYGraph on which this toolbar operates
     *  @param flags Bitwise 'or' of flags
     *  @see XYGraphFlags#COMBINED_ZOOM
     *  @see XYGraphFlags#SEPARATE_ZOOM
	 */
	public XYGraphToolbar(final XYGraph xyGraph, final int flags) {		
		this.xyGraph = xyGraph;
		setLayoutManager(new WrappableToolbarLayout());
		
		final Button configButton = new Button(createImage("icons/Configure.png"));
		configButton.setToolTip(new Label("Configure Settings..."));
		addButton(configButton);
		configButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				openConfigurationDialog();
			}
		});
		
		final ToggleButton showLegend = new ToggleButton("", createImage("icons/ShowLegend.png"));
		showLegend.setToolTip(new Label("Show Legend"));
		addButton(showLegend);
		showLegend.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				xyGraph.setShowLegend(!xyGraph.isShowLegend());
			}
		});
		
		showLegend.setSelected(xyGraph.isShowLegend());
		
		addSeparator("org.csstudio.swt.xygraph.toolbar.showLegend");	
		final Button addAnnotationButton = new Button(createImage("icons/Add_Annotation.png"));
		addAnnotationButton.setToolTip(new Label("Add Annotation..."));		
		addButton(addAnnotationButton);
		addAnnotationButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				AddAnnotationDialog dialog = new AddAnnotationDialog(
						Display.getCurrent().getActiveShell(), xyGraph);
				if(dialog.open() == Window.OK){
					xyGraph.addAnnotation(dialog.getAnnotation());
					xyGraph.getOperationsManager().addCommand(
							new AddAnnotationCommand(xyGraph, dialog.getAnnotation()));
				}
			}
		});
		
		final Button delAnnotationButton = new Button(createImage("icons/Del_Annotation.png"));
		delAnnotationButton.setToolTip(new Label("Remove Annotation..."));
		addButton(delAnnotationButton);
		delAnnotationButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				RemoveAnnotationDialog dialog = new RemoveAnnotationDialog(
						Display.getCurrent().getActiveShell(), xyGraph);
				if(dialog.open() == Window.OK && dialog.getAnnotation() != null){
					xyGraph.removeAnnotation(dialog.getAnnotation());
					xyGraph.getOperationsManager().addCommand(
							new RemoveAnnotationCommand(xyGraph, dialog.getAnnotation()));					
				}
			}
		});
		
		addSeparator("org.csstudio.swt.xygraph.toolbar.annotation");	
		addSeparator("org.csstudio.swt.xygraph.toolbar.extra");	
				
		if ((flags & XYGraphFlags.STAGGER) > 0)
		{	//stagger axes button
    		final Button staggerButton = new Button(createImage("icons/stagger.png"));
    		staggerButton.setToolTip(new Label("Stagger axes so they don't overlap"));
    		addButton(staggerButton);
    		staggerButton.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent event) {
    				xyGraph.performStagger();
    			}
    		});
		}
		else
		{	//auto scale button
            final Button autoScaleButton = new Button(createImage("icons/AutoScale.png"));
            autoScaleButton.setToolTip(new Label("Perform Auto Scale"));
            addButton(autoScaleButton);
            autoScaleButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent event) {
                    xyGraph.performAutoScale();
                }
            });
		}
		
		//zoom buttons
		zoomGroup = new ButtonGroup();
		createZoomButtons(flags);
	
		addSeparator("org.csstudio.swt.xygraph.toolbar.undoredo");		
		addUndoRedoButtons();
		
		addSeparator("org.csstudio.swt.xygraph.toolbar.snapshot");		
		addSnapshotButton();
	}
	
	protected static Image createImage(String path) {			
		Image image = XYGraphMediaFactory.getInstance().getImage(path);				
		return image;
	}
	
	protected void addSnapshotButton() {
		Button snapShotButton = new Button(createImage("icons/camera.gif"));
		snapShotButton.setToolTip(new Label("Print scaled image to printer"));
		addButton(snapShotButton);
		snapShotButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {

                // Show the Choose Printer dialog
                PrintDialog dialog = new PrintDialog(Display.getCurrent().getActiveShell(), SWT.NULL);
                PrinterData printerData = dialog.open();
                printerData.orientation = PrinterData.LANDSCAPE; // force landscape
                
                if (printerData != null) {
                  // Create the printer object
                  Printer printer = new Printer(printerData);
          
                  // Calculate the scale factor between the screen resolution and printer
                  // resolution in order to correctly size the image for the printer
                  Point screenDPI = Display.getCurrent().getDPI();
                  Point printerDPI = printer.getDPI();
                  
                  int scaleFactorX = printerDPI.x / screenDPI.x;
                   
          
                  // Determine the bounds of the entire area of the printer
                  Rectangle size = printer.getClientArea();
                  Rectangle trim = printer.computeTrim(0, 0, 0, 0);    
                  
                  Rectangle imageSize = new Rectangle(size.x/scaleFactorX, size.y/scaleFactorX, 
                		                              size.width/scaleFactorX, size.height/scaleFactorX);

                  if (printer.startJob("Print Plot")) {
                	  if (printer.startPage()) {
                		  GC gc = new GC(printer);

                          Image xyImage      = xyGraph.getImage(imageSize);
                          Image printerImage = new Image(printer, xyImage.getImageData());
                          xyImage.dispose();
                          
                		  // Draw the image
                		  gc.drawImage(printerImage, imageSize.x, imageSize.y,
                				                     imageSize.width, 
                				                     imageSize.height,               				                     
                				                     -trim.x, -trim.y,
                				                     size.width-trim.width, 
                				                     size.height-trim.height);

                		  // Clean up
                		  printerImage.dispose();
                		  gc.dispose();
                		  printer.endPage();
                	  }
                  }
                  // End the job and dispose the printer
                  printer.endJob();
                  printer.dispose();
                }
			}
		});
	}

	private void addUndoRedoButtons() {
		//undo button		
		final GrayableButton undoButton = new GrayableButton(createImage("icons/Undo.png"));
		undoButton.setToolTip(new Label("Undo"));
		undoButton.setEnabled(false);
		addButton(undoButton);		
		undoButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				xyGraph.getOperationsManager().undo();
			}
		});
		xyGraph.getOperationsManager().addListener(new IOperationsManagerListener(){
			public void operationsHistoryChanged(OperationsManager manager) {
				if(manager.getUndoCommandsSize() > 0){
					undoButton.setEnabled(true);
					final String cmd_name = manager.getUndoCommands()[
					           manager.getUndoCommandsSize() -1].toString();
                    undoButton.setToolTip(new Label(NLS.bind("Undo {0}", cmd_name)));
				}else{
					undoButton.setEnabled(false);
					undoButton.setToolTip(new Label("Undo"));
				}			
			}
		});
		
		// redo button
		final GrayableButton redoButton = new GrayableButton(createImage("icons/Redo.png"));
		redoButton.setToolTip(new Label("Redo"));
		redoButton.setEnabled(false);
		addButton(redoButton);		
		redoButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				xyGraph.getOperationsManager().redo();
			}
		});
		xyGraph.getOperationsManager().addListener(new IOperationsManagerListener(){
			public void operationsHistoryChanged(OperationsManager manager) {
				if(manager.getRedoCommandsSize() > 0){
					redoButton.setEnabled(true);
					final String cmd_name = manager.getRedoCommands()[
					           manager.getRedoCommandsSize() -1].toString();
                    redoButton.setToolTip(new Label(NLS.bind("Redo {0}", cmd_name)));
				}else{
					redoButton.setEnabled(false);
					redoButton.setToolTip(new Label("Redo"));
				}					
			}
		});
	}
	
	/** Create buttons enumerated in <code>ZoomType</code>
     *  @param flags Bitwise 'or' of flags
     *  @see XYGraphFlags#COMBINED_ZOOM
     *  @see XYGraphFlags#SEPARATE_ZOOM
	 */
	protected void createZoomButtons(final int flags) {
		for(final ZoomType zoomType : ZoomType.values()){
		    if (! zoomType.useWithFlags(flags)) continue;
		    
			final ImageFigure imageFigure =  new ImageFigure(zoomType.getIconImage());
			final Label tip = new Label(zoomType.getDescription());
			final ToggleButton button = new ToggleButton(imageFigure);
			button.setBackgroundColor(ColorConstants.button);
			button.setOpaque(true);
			final ToggleModel model = new ToggleModel();
			model.addChangeListener(new ChangeListener(){
				public void handleStateChanged(ChangeEvent event) {
					if(event.getPropertyName().equals("selected") && 
							button.isSelected()){
						xyGraph.setZoomType(zoomType);
					}				
				}
			});
			
			button.setModel(model);
			button.setToolTip(tip);
			addButton(button);
			zoomGroup.add(model);
			
			if(zoomType == ZoomType.NONE)
				zoomGroup.setDefault(model);
		}
	}
	
	protected void openConfigurationDialog() {
		XYGraphConfigDialog dialog = new XYGraphConfigDialog(
				Display.getCurrent().getActiveShell(), xyGraph);
		dialog.open();
	}
	
	public void addButton(Clickable button){
		button.setPreferredSize(BUTTON_SIZE, BUTTON_SIZE);
		add(button);
	}
	
	public void addSeparator(final String id) {
		ToolbarSeparator separator = new ToolbarSeparator(id);
		separator.setPreferredSize(BUTTON_SIZE/2, BUTTON_SIZE);
		add(separator);
	}
	
	protected static final class ToolbarSeparator extends Figure{
		
		protected String id;
		ToolbarSeparator(String id) {
			this.id = id;
		}
		
		private final Color GRAY_COLOR = XYGraphMediaFactory.getInstance().getColor(
				new RGB(130, 130, 130));
		
		@Override
		protected void paintClientArea(Graphics graphics) {
			super.paintClientArea(graphics);
			graphics.setForegroundColor(GRAY_COLOR);
			graphics.setLineWidth(1);
			graphics.drawLine(bounds.x + bounds.width/2, bounds.y, 
					bounds.x + bounds.width/2, bounds.y + bounds.height);
		}
		
		public String getId() {
			return id;
		}
	}

	/**
	 * Bodges up a normal toolbar from the Figure toolbar.
	 * @param xyGraph
	 * @param man
	 */
	public void createGraphActions(final IContributionManager tool, final IContributionManager men) {
        
        final CheckableActionGroup zoomG = new CheckableActionGroup();
        
        for (Object child : getChildren()) {
			
        	if (!(child instanceof Figure)) continue;
        	final Figure c = (Figure)child;
        	if (c instanceof Clickable) {
        		
        		final Clickable button = (Clickable)c;
        		final int flag = button instanceof ToggleButton
        		               ? IAction.AS_CHECK_BOX
        		               : IAction.AS_PUSH_BUTTON;
        		
        		final String text  = ((Label)button.getToolTip()).getText();
        		
        		final Object cont  = button.getChildren().get(0);
        		final Image  image = cont instanceof ImageFigure
        		                   ? ((ImageFigure)cont).getImage()
        		                   : ((Label)cont).getIcon();
        		                   
        		final Action action = new Action(text, flag) {
        			public void run() {
        				if (button.getModel() instanceof ToggleModel) {
        					((ToggleModel)button.getModel()).fireActionPerformed();
        				} else {
        				    button.doClick();
        				}        				
        			}
				};
				 
				if (flag == IAction.AS_CHECK_BOX) {
					final boolean isSel = button.isSelected();
					action.setChecked(isSel);
				}

				if (button instanceof GrayableButton) {
					final GrayableButton gb = (GrayableButton)button;
					gb.addChangeListener(new ChangeListener() {	
						@Override
						public void handleStateChanged(ChangeEvent event) {
							if (event.getPropertyName().equals(ButtonModel.ENABLED_PROPERTY)) {
                                action.setEnabled(gb.isEnabled());
							}
						};
					});

				};
        				
				action.setImageDescriptor(new ImageDescriptor() {			
					@Override
					public ImageData getImageData() {
						return image.getImageData();
					}
				});
				
				tool.add(action);
				men.add(action);
        	    final List models = zoomGroup.getElements();
        	    if (models.contains(button.getModel())) {
        	    	zoomG.add(action);
        	    }
        	    
        	} else if (c instanceof ToolbarSeparator) {
        		
           		tool.add(new Separator(((ToolbarSeparator)c).getId()));
           		men.add(new Separator(((ToolbarSeparator)c).getId()));
        	}
		}
	}
}
