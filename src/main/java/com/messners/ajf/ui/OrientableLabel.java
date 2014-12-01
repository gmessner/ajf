package com.messners.ajf.ui;

// java core classes needed
import javax.swing.JLabel;
import javax.swing.plaf.LabelUI;


/**
 * A JLabel that can be oriented vertically as well as horizontally
 */
public class OrientableLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	/* property key for orientation ("orientation")*/
	public final static String ORIENTATION_PROP_KEY = "orientation";
	
	/* a handle to our UI (renderer) */
	private OrientableLabelUI ui = null;

	
	/** main constructor */
	public OrientableLabel(){
		this.setUI(new OrientableLabelUI());
	}
	
	/** convenience constructor */
	public OrientableLabel(String text){
		this();
		setText(text);
	}
	
	/* override */
	public void setUI(LabelUI newUI){
		if(newUI instanceof OrientableLabelUI){
			super.setUI(newUI);
			ui = (OrientableLabelUI)newUI;
		}
		// else ignore
	}
	
	/**
	* sets the label's orientation
	* @param newOrientation either JLabel.HORIZONTAL or JLabel.VERTICAL
	*/
	public void setOrientation( int newOrientation){
		if(getOrientation() == newOrientation) return; // no change
		//
		// save off old val for event
		Integer oldValue = new Integer(getOrientation());
		Integer newValue = new Integer(newOrientation);
		// accept change
		ui.setOrientation(newOrientation);
		// notify interested parties of the change
		firePropertyChange(ORIENTATION_PROP_KEY, oldValue, newValue);
		//
		revalidate();
		//repaint();
	}
	
	public int getOrientation(){
		return ui.getOrientation();
	}

}
