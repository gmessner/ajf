package com.messners.ajf.ui;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import java.util.Stack;

/**
 * Default implementation of the <code>WizardStep</code> interface for
 * convience.
 */
public class DefaultWizardStep extends JPanel 
         implements WizardStep, UIConstants{

	private static final long serialVersionUID = 1L;

	/**
	 * Construct the panel with the specified name and resource loader.
	 * The name is used to load resources common to all wizard steps: 
	 * Sub-Title (name + ".subTitle"), Main Instructions (name + 
	 * ".mainInstr"), and Step Text (name + * ".stepText").  For example,
	 * if the <code>name</code> is "foo", then the Sub-Title can be 
	 * defined by <code>foo.subTitle=The * Sub-Title Of This Wizard 
	 * Step</code> in the resource file.  <B>All child classes must 
	 * invoke this method within its constructor.</B>
    * 
	 * @param name Name of the panel.
	 * @param uiLoader Resource loader used to load resources.
	 */
	protected DefaultWizardStep (String name, ResourceLoader uiLoader) {

		// Set the name of this panel (req. for navigation).
		setName(name);

		// Load components from resources.
		TextLabel subTitle = uiLoader.loadTextLabel
					(name + ".subTitle");
		mainInstr = uiLoader.loadTextLabel(name + ".mainInstr");
      if (mainInstr == null) {
         mainInstr = new TextLabel("");
      }
		stepTxt = uiLoader.getResource(name + ".stepTxt");
            
		// Build the panel.
		ColumnLayout layout = new ColumnLayout(0, 0);
		layout.setVgap(0);
		layout.setYSpacing(0);
		layout.setExpandWidth(true);
		setLayout(layout);

		add(subTitle);
		JSeparator subTitleSep = new JSeparator();
		add(Box.createVerticalStrut(VGAP));
		subTitleSep.setBackground(subTitle.getForeground());
		add(subTitleSep);
		add(Box.createVerticalStrut(TITLED_PANEL_INNER_BOTTOM_MARGIN));
		add(mainInstr);
	}
   

   /**
    * Sets the main instructions to be displayed on the wizard step.
    *
    * @param text Main instructions to set.
    */
   protected void setMainInstructions (String text) {

      mainInstr.setText(text);
   }


   /**
    * Gets the main instructions displayed on the wizard step.
    *
    * @return Main instruction text.
    */
   protected String getMainInstructions () {

      return mainInstr.getText();
   }


	/**
	 * Sets the user input panel for the WizardStep.
    * 
	 * @param pnl User input panel to set.
	 */
	protected void setInputPanel (DefaultInputPanel pnl) {

		if (inpPnl != null) {
			remove(inpPnl);
		}
		inpPnl = pnl;
		add(inpPnl);
	}
   

	/**
	 * Sets the stack used to keep track of previously visited steps.
    * 
	 * @param history Stack of previously visited steps.
	 */
	public void setHistory (Stack<DefaultWizardStep> history) {

		prvPnls = history;
	}

   
	/**
	 * Sets the parent wizard manager.
    * 
	 * @param parent Parent wizard manager.
	 */
	public void setParent (WizardManager parent) {

		parMgr = parent;
	}

   
	/**
	 * Gets the container for user input, <B>child classes should overide this
	 * method if they want anything to work</B>.
    * 
	 * @return Container for user input.
	 */
	public Object getData () {

		return null;
	}
   

	/**
	 * Sets the container for user input, <B>child classes should overide this
	 * method if they want anything to work</B>.
    * 
	 * @param data Container for user input.
	 */
	public void setData (Object data) {

	}

   
	/**
	 * Allow advancement to the next panel.
    * 
	 * @return <code>true</code> if it's okay to proceed to the next panel.
	 */
	public boolean allowNext () {

		return (inpPnl.checkInput());
	}
   

   /**
    * Checks if user input is being preserved.
    * 
    * @return If user input is preserved, then returns <code>true</code>.
    */
   public boolean getPreserveInput () {

      return preserveInput;
   }
   

   /**
    * Resets the state of the WizardStep.
    */
   public void reset () {

      preserveInput = false;
   }
   

	// From WizardStep.
	public void enter (int direction) {

		if (inpPnl == null) {
			return; // Nothing to do.
		}

		// Clear all input.
		if (direction == FORWARD) {
			Object data = getData();
         if (lastData != null) {
            preserveInput = (lastData == data);
         }
         lastData = data;
         if (!preserveInput) {
			   inpPnl.clear();
			   if (data != null) {
				   inpPnl.populate(data);
			   }
         }
		} 
	}
   

	// From WizardStep.
	public boolean leave (int direction) {

		boolean okToLeave = true;
		if (direction == FORWARD) {
			if (inpPnl != null) {
				okToLeave = inpPnl.validateInput();
				if (okToLeave) {
					setData(inpPnl.getInput(getData()));
               preserveInput = false;
				}
			}
			if (okToLeave) {
				prvPnls.push(this);
			}
		} else if (direction == BACK) {
			if (inpPnl != null) {
            // Can't save user input, preserve it in case user revisits.
            preserveInput = true;
			}
			if (prvPnls != null) {
				DefaultWizardStep pnl = 
					prvPnls.pop();
				if (parMgr != null) {
					parMgr.skipTo(pnl.getName());
					okToLeave = false; // Req. for skip.
				}
			}
		}
		return okToLeave;
	}

   
	// From WizardStep.
	public Component getComponent () {

		return (Component) this;
	}

   
	// From WizardStep.
	public String getStep () {

		return stepTxt;
	}

   
	/** Text which describes this wizard step. */
	private String stepTxt = null;

	/** Text which describes the main instructions of this wizard step. */
   private TextLabel mainInstr = null;

	/** User input panel. */
	private DefaultInputPanel inpPnl = null;

	/** Stack of previously visited panels. */
	private Stack<DefaultWizardStep> prvPnls = null;

	/** Parent wizard manager. */
	private WizardManager parMgr = null;

   /** Flag which indicates that user input should be preserved. */
   private boolean preserveInput = false;

   /** Last data object to visit this wizard step. */
   private Object lastData = null;
}
