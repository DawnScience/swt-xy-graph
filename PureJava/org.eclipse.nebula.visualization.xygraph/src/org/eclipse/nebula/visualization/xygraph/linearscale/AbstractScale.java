package org.eclipse.nebula.visualization.xygraph.linearscale;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;


/**
 * The abstract scale has the common properties for linear(straight) scale and 
 * round scale.
 * @author Xihui Chen
 *
 */
public abstract class AbstractScale extends Figure{	
	

    /** ticks label's position relative to tick marks*/
    public enum LabelSide {

        /** bottom or left side of tick marks for linear scale, 
         *  or outside for round scale */
        Primary,

        /** top or right side of tick marks for linear scale, 
         *  or inside for round scale*/
        Secondary
    }


	public static final double DEFAULT_MAX = 100d;


	public static final double DEFAULT_MIN = 0d;

	/**
	 * the digits limit to be displayed in engineering format
	 */
	public static final int ENGINEERING_LIMIT = 4;

	public static final String DEFAULT_ENGINEERING_FORMAT = "0.####E0";

	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd\nHH:mm:ss";    	
    
    /** ticks label position */
    private LabelSide tickLabelSide = LabelSide.Primary;   


    /** the default minimum value of log scale range */
    public final static double DEFAULT_LOG_SCALE_MIN = 0.0001d;

    /** the default maximum value of log scale range */
    public final static double DEFAULT_LOG_SCALE_MAX = 100d;
    
    /** the default label format */
    private String default_decimal_format = "############.##";    
	
    /** the state if the axis scale is log scale */
    protected boolean logScaleEnabled = false;
    
	/** The minimum value of the scale */
    protected double min = DEFAULT_MIN;
	
	/** The maximum value of the scale */
	protected double max = DEFAULT_MAX;	

	 /** the format for tick labels */
     private String formatPattern;
    
    /** the time unit for tick step */
    private int timeUnit = 0;
    
     /** Whenever any parameter has been  changed, the scale should be marked as dirty, 
      * so all the inner parameters could be recalculated before the next paint*/
    private boolean dirty = true;

    private boolean dateEnabled = false;
    
    private boolean scaleLineVisible = true;

	/** the pixels hint for major tick mark step */
    private int majorTickMarkStepHint = 30;
    
    /** the pixels hint for minor tick mark step */
    private int minorTickMarkStepHint = 4;
    
    private boolean minorTicksVisible= true;
    
    private double majorGridStep = 0;

    private boolean autoFormat = true;
	
    protected Range range = new Range(min, max);

    private Map<Integer, Format> cachedFormats = new HashMap<Integer, Format>();

    private boolean ticksAtEnds = true;

    /**
     * Formats the given object.
     * 
     * @param obj
     *            the object
     * @return the formatted string
     */
    public String format(Object obj) {     
    	return format(obj, 0);
    }

    /**
     * Formats the given object.
     * 
     * @param obj
     *            the object
     * @param extraDP must be non-negative
     * @return the formatted string
     */
    public String format(Object obj, int extraDP) {     
    	if (extraDP < 0) {
    		throw new IllegalArgumentException("Number of extra decimal places must be non-negative");
    	}
		if (cachedFormats.get(extraDP) == null) {
			if (isDateEnabled()) {
				if (autoFormat || formatPattern == null
						|| formatPattern.equals("")
						|| formatPattern.equals(default_decimal_format)
						|| formatPattern.equals(DEFAULT_ENGINEERING_FORMAT)) {
					formatPattern = DEFAULT_DATE_FORMAT; // (?) overridden anyway
					double length = Math.abs(max - min);
					if (length <= 1000 || timeUnit == Calendar.MILLISECOND) { // less than a second
						formatPattern = "HH:mm:ss.SSS";
					} else if (length <= 3600000d
							|| timeUnit == Calendar.SECOND) { // less than a hour
						formatPattern = "HH:mm:ss";
					} else if (length <= 86400000d
							|| timeUnit == Calendar.MINUTE) { // less than a day
						formatPattern = "HH:mm";
					} else if (length <= 604800000d
							|| timeUnit == Calendar.HOUR_OF_DAY) { // less than a week
						formatPattern = "dd HH:mm";
					} else if (length <= 2592000000d
							|| timeUnit == Calendar.DATE) { // less than a month
						formatPattern = "MMMMM d";
					} else if (length <= 31536000000d
							|| timeUnit == Calendar.MONTH) { // less than a year
						formatPattern = "yyyy MMMMM";
					} else {// if (timeUnit == Calendar.YEAR) {
						formatPattern = "yyyy";
					}
					if (formatPattern == null || formatPattern.equals("")) {
						autoFormat = true;
					}
				}
				cachedFormats.put(extraDP, new SimpleDateFormat(formatPattern));
			} else {
				if (formatPattern == null || formatPattern.isEmpty() || formatPattern.equals(default_decimal_format) || formatPattern.equals(DEFAULT_DATE_FORMAT)) {
					formatPattern = getAutoFormat(min, max);
					if (formatPattern == null || formatPattern.equals("")) {
						autoFormat = true;
					}
				}

				String ePattern = formatPattern;
				if (extraDP > 0) {
					int e = formatPattern.lastIndexOf('E');
					StringBuilder temp = new StringBuilder(e == -1 ? formatPattern : formatPattern.substring(0, e));
					for (int i = 0; i < extraDP; i++) {
						temp.append('#');
					}
					if (e != -1) {
						temp.append(formatPattern.substring(e));
					}
					ePattern = temp.toString();
				}
				cachedFormats.put(extraDP, new DecimalFormat(ePattern));
			}
		}

		if (isDateEnabled() && obj instanceof Number) {
			return cachedFormats.get(extraDP).format(new Date(((Number) obj).longValue()));
		}
		return cachedFormats.get(extraDP).format(obj);
    }

    protected String getAutoFormat(double min, double max) {
    	ITicksProvider ticks = getTicksProvider();
    	if (ticks == null) {
			if ((max != 0 && Math.abs(Math.log10(Math.abs(max))) >= ENGINEERING_LIMIT)
						|| (min != 0 && Math.abs(Math.log10(Math.abs(min))) >= ENGINEERING_LIMIT)) {
					return DEFAULT_ENGINEERING_FORMAT;
			}
			return default_decimal_format;
    	}
    	return ticks.getDefaultFormatPattern(min, max);
    }

	/**
	 * Gets the ticks provider
	 * @return tick provider
	 */
    abstract public ITicksProvider getTicksProvider();

    /**
	 * @return the majorTickMarkStepHint
	 */
	public int getMajorTickMarkStepHint() {
		return majorTickMarkStepHint;
	}

	/** get the scale range */ 
    public Range getRange() {
        return range;
    }

	
	/**
	 * @return the side of the tick label relative to the tick marks
	 */
	public LabelSide getTickLabelSide() {
		return tickLabelSide;
	}

	/**
	 * @return the timeUnit
	 */
	public int getTimeUnit() {
		return timeUnit;
	}


	/**
	 * @return the dateEnabled
	 */
	public boolean isDateEnabled() {
		return dateEnabled;
	}

	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}


    private boolean userDefinedFormat = false;


	protected boolean forceRange;

	/**
     * Gets the state indicating if log scale is enabled.
     * 
     * @return true if log scale is enabled
     */
    public boolean isLogScaleEnabled() {
        return logScaleEnabled;
    }

	
	/**
	 * @return the minorTicksVisible
	 */
	public boolean isMinorTicksVisible() {
		return minorTicksVisible;
	}

	
    /**
	 * @return the scaleLineVisible
	 */
	public boolean isScaleLineVisible() {
		return scaleLineVisible;
	}

	/**
	 * @param dateEnabled the dateEnabled to set
	 */
	public void setDateEnabled(boolean dateEnabled) {
		this.dateEnabled = dateEnabled;
		cachedFormats.clear();
        setDirty(true);
        revalidate();
	}

	/** Whenever any parameter has been changed, the scale should be marked as dirty, 
     * so all the inner parameters could be recalculated before the next paint
	 * @param dirty the dirty to set
	 */
	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
     * Sets the format pattern for axis tick label. see {@link Format}
     * <p>
     * If <tt>null</tt> is set, default format will be used.
     * 
     * @param format
     *            the format
	 * @exception NullPointerException if <code>pattern</code> is null
     * @exception IllegalArgumentException if the given pattern is invalid.
     */
    public void setFormatPattern(String formatPattern) {
    	
    	this.userDefinedFormat = true;
    	setFormat(formatPattern);
    }
    
    public void setDefaultFormatPattern(String formatPattern) {
    	setFormat(formatPattern);
    }
    
	protected void setFormat(String formatPattern) {
		try {
 			new DecimalFormat(formatPattern);
 		} catch (NullPointerException e) {
 			throw e;
 		} catch (IllegalArgumentException e){
 			throw e;
 		}
		cachedFormats.clear();
        this.formatPattern = formatPattern;
       
        autoFormat = false;
        setDirty(true);
        revalidate();
        repaint();		
	}

	public boolean hasUserDefinedFormat() {
		return userDefinedFormat;
	}

	/**
	 * @return the formatPattern
	 */
	public String getFormatPattern() {
		return formatPattern;
	}
	
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		setDirty(true);
		revalidate();
	}

	/**
     * @param enabled true if enabling log scales
     * @throws IllegalStateException
     */
    public void setLogScale(boolean enabled) throws IllegalStateException {

        if (logScaleEnabled == enabled) {
            return;
        } 
        
        if(enabled) {
        	if(min == DEFAULT_MIN && max == DEFAULT_MAX) {
        		min = DEFAULT_LOG_SCALE_MIN;
        		max = DEFAULT_LOG_SCALE_MAX;       			
        	}
        	if(max <= 0) {
        		max = DEFAULT_LOG_SCALE_MAX;
        	}
        	if(min <= 0) {
        		min = DEFAULT_LOG_SCALE_MIN * max;
        	}
        	if(max <= min) {
        		max = min + DEFAULT_LOG_SCALE_MAX;
        	}
        } else if(min == DEFAULT_LOG_SCALE_MIN && max == DEFAULT_LOG_SCALE_MAX) {
        	min = DEFAULT_MIN;
        	max = DEFAULT_MAX;
        }
        	
        logScaleEnabled = enabled;
        setTicksAtEnds(true);
        range = new Range(min, max);
        setDirty(true);
		revalidate();
		repaint();
    }

	/**
	 * @param majorTickMarkStepHint the majorTickMarkStepHint to set, should be less than 1000.
	 */
	public void setMajorTickMarkStepHint(int majorTickMarkStepHint) {		
		this.majorTickMarkStepHint = majorTickMarkStepHint;
		setDirty(true);
		revalidate();
		repaint();
	}

	/**
	 * @param minorTicksVisible the minorTicksVisible to set
	 */
	public void setMinorTicksVisible(boolean minorTicksVisible) {
		this.minorTicksVisible = minorTicksVisible;
	}

    /** set the scale range */
	public void setRange(final Range range) {
	    if (range == null) {
	        SWT.error(SWT.ERROR_NULL_ARGUMENT);
	        return; // to suppress warnings...
	    }
	    setRange(range.getLower(), range.getUpper());
	}

	private static final double ZERO_RANGE_FRACTION = 0.125; // used if difference between min and max is too small

	/**set the scale range
     * @param lower the lower limit
     * @param upper the upper limit
     * @throws IllegalArgumentException  
     * if lower or upper is Nan of Infinite, or lower >= upper or (upper - lower) is Infinite  
     */
    public void setRange(double lower, double upper){
        if (Double.isNaN(lower) || Double.isNaN(upper) 
        		|| Double.isInfinite(lower) || Double.isInfinite(upper) || Double.isInfinite(upper-lower)) {
            throw new IllegalArgumentException("Illegal range: lower=" + lower + ", upper=" + upper);
        }

        forceRange = lower == upper; 
        if (forceRange) {
            final double delta = (lower == 0 ? 1 : Math.abs(lower)) * ZERO_RANGE_FRACTION;
        	upper += delta;
        	lower -= delta;
        	if(Double.isInfinite(upper))
                throw new IllegalArgumentException("Illegal range: lower=" + lower + ", upper=" + upper);
        }

        if (logScaleEnabled) {
        	if (upper <= 0)
        		upper = DEFAULT_LOG_SCALE_MAX;
        	if (lower <= 0)
        		lower = DEFAULT_LOG_SCALE_MIN * upper;
        }

        min = lower;
        max = upper;
        range = new Range(min, max);
        cachedFormats.clear();
        setDirty(true);
        revalidate();
        repaint();
    }


	/**
	 * @param scaleLineVisible the scaleLineVisible to set
	 */
	public void setScaleLineVisible(boolean scaleLineVisible) {
		this.scaleLineVisible = scaleLineVisible;
	}

	/**
	 * @param tickLabelSide the side of the tick label relative to tick mark
	 */
	public void setTickLabelSide(LabelSide tickLabelSide) {
		this.tickLabelSide = tickLabelSide;
		revalidate();
	}

	/**Set the time unit for a date enabled scale. The format of the time
     * would be determined by it.
	 * @param timeUnit the timeUnit to set. It should be one of: 
	 * <tt>Calendar.MILLISECOND</tt>, <tt>Calendar.SECOND</tt>, 
	 * <tt>Calendar.MINUTE</tt>, <tt>Calendar.HOUR_OF_DAY</tt>, 
	 * <tt>Calendar.DATE</tt>, <tt>Calendar.MONTH</tt>, 
	 * <tt>Calendar.YEAR</tt>.
	 * @see Calendar
	 */
	public void setTimeUnit(int timeUnit) {
		this.timeUnit = timeUnit;
        setDirty(true);
	}

	/**
     * Updates the tick, recalculate all inner parameters
     */
    public abstract void updateTick();

	/**
	 * @param majorGridStep the majorGridStep to set
	 */
	public void setMajorGridStep(double majorGridStep) {
		this.majorGridStep = majorGridStep;
		setDirty(true);
	}

	/**
	 * @return the majorGridStep
	 */
	public double getMajorGridStep() {
		return majorGridStep;
	}

	/**
	 * @param minorTickMarkStepHint the minorTickMarkStepHint to set
	 */
	public void setMinorTickMarkStepHint(int minorTickMarkStepHint) {
		this.minorTickMarkStepHint = minorTickMarkStepHint;
	}

	/**
	 * @return the minorTickMarkStepHint
	 */
	public int getMinorTickMarkStepHint() {
		return minorTickMarkStepHint;
	}

	/**
	 * @param autoFormat the autoFormat to set
	 */
	public void setAutoFormat(boolean autoFormat) {
		this.autoFormat = autoFormat;
		if(autoFormat){
			formatPattern = null;
			cachedFormats.clear();
			setRange(getRange());
			format(0);
		}
	}

	/**
	 * @return the autoFormat
	 */
	public boolean isAutoFormat() {
		return autoFormat;
	}

	public boolean hasTicksAtEnds() {
		return ticksAtEnds;
	}

	public void setTicksAtEnds(boolean ticksAtEnds) {
		this.ticksAtEnds = ticksAtEnds;
	}
}
