
package examples.actions;

import com.messners.ajf.ui.AbstractAppAction;

import examples.SampleApp;

import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.JComponent;


public class RunningAction extends AbstractAppAction {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "running";
	SampleApp _app = null;

	public RunningAction (SampleApp app) {
		super(NAME);
		_app = app;
	}


	public void actionPerformed (ActionEvent e) {

		Object src = e.getSource();
		if (src instanceof AbstractButton) {

			boolean flag = ((AbstractButton)src).isSelected();
			int num_components = getNumComponents();
			for (int i = 0; i < num_components; i++) {

				JComponent c = getComponentAt(i);
				if (c instanceof AbstractButton) {
					((AbstractButton)c).setSelected(flag);
				}		
			}

			_app.setProcessing(flag);
		}
	}
}
