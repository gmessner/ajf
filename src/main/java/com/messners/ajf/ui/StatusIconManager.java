package com.messners.ajf.ui;

import com.messners.ajf.reflect.MethodUtils;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


/**
 * TODO: This class needs javadoc.
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class StatusIconManager extends JPanel {

	private static final long serialVersionUID = 1L;
	private HashMap<Icon, JLabel> buttonMap;

	public StatusIconManager () {

		super();
		buttonMap = new HashMap<Icon, JLabel>();
		setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 0));
	}


	public void add (
		Icon icon, String tooltip, JTabbedPane tabbedPane, JComponent tab) {

		JLabel btn = new JLabel(icon);
		btn.setOpaque(true);
		if (tooltip != null) {
			btn.setToolTipText(tooltip);
		}

		btn.addMouseListener(new TabAction(btn, tabbedPane, tab));
		buttonMap.put(icon, btn);
	}


	public void add (
		Icon icon, String tooltip, Object obj, String showMethodName) {

		JLabel btn = new JLabel(icon);
		btn.setOpaque(true);
		if (tooltip != null) {
			btn.setToolTipText(tooltip);
		}

		try {
			btn.addMouseListener(new MethodAction(btn, obj, showMethodName));
		} catch (Exception e) {
			throw (new RuntimeException(
						"Couldn't get Method instance: " + e.getMessage()));
		}

		buttonMap.put(icon, btn);
	}


	public void show (Icon icon, boolean visible) {

		JLabel btn = buttonMap.get(icon);
		if (btn != null) {

			remove(btn);
			if (visible) {
				add(btn);
			}

			revalidate();
		}
	}


	protected class TabAction extends MouseAdapter {

		private JLabel btn;
		private JTabbedPane tabbedPane;
		private JComponent tab;

		public TabAction (JLabel btn, JTabbedPane tabbedPane, JComponent tab) {

			this.btn = btn;
			this.tabbedPane = tabbedPane;
			this.tab = tab;
		}

	
		public void mouseClicked (MouseEvent me) {

			tabbedPane.setSelectedComponent(tab);
			remove(btn);
			revalidate();
		}
	}


	protected class MethodAction extends MouseAdapter {

		private Method showMethod;
		private JLabel btn;
		private Object obj;

		public MethodAction (JLabel btn, Object obj, String showMethodName) 
			throws NoSuchMethodException, IllegalAccessException,
					   InvocationTargetException {

			showMethod = MethodUtils.getMethod(
				obj.getClass(), showMethodName, (Class<?>)null);

			this.btn = btn;
			this.obj = obj;
		}


		public void mouseClicked (MouseEvent me) {

			try {
				showMethod.invoke(obj, (Object)null);
			} catch (Exception e) {
			}

			remove(btn);
			revalidate();
		}
	}
}

