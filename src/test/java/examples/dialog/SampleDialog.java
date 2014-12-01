
package examples.dialog;

import com.messners.ajf.ui.DialogForm;
import com.messners.ajf.ui.PropertyList;
import com.messners.ajf.ui.ResourceLoader;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;


public class SampleDialog extends DialogForm {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "sample-dialog";

	protected PropertyList _prop_list;
	protected ResourceLoader _loader;

	public SampleDialog () {

		super();
		_loader = new ResourceLoader(this.getClass());

		setBorder(BorderFactory.createEmptyBorder(8, 4, 4, 4));
		setLayout(new BorderLayout());
		setTitle(_loader.getResource(NAME + ".title"));

		_prop_list = new PropertyList("options", _loader);
		JScrollPane scroller = new JScrollPane(_prop_list);

		Dimension d = scroller.getPreferredSize();
		if (d.height > 256) {
			d.height = 256;
			scroller.setPreferredSize(d);
		}

		add(scroller);
	}


	/**
	 * Set initial form values.
	 */
	public void reset () {

	}


	public boolean apply () {

		return (true);
	}
}
