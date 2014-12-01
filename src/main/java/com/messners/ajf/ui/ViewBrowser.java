package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


/**
 * <p>This class can be used to create a basic view browser. A view
 * browser is usually composed of a tree on the left side and a view
 * panel on the right.</p>
 *
 * @author  Greg Messner <greg@messners.com>
 */
public class ViewBrowser extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final int ICON_POSITION_LEFT  = ViewTitle.LEFT;
	public static final int ICON_POSITION_RIGHT = ViewTitle.RIGHT;

	protected JSplitPane splitPane;
	protected JPanel viewPanel;
	protected JPanel views;
	protected CardLayout viewsManager;
	protected JComponent browser;

	protected JComponent currentViewComponent;
	protected ViewTitle viewTitlePanel;

	protected HashMap<JComponent, String> componentMap;
	


	/**
	 * Default no-args constructor.
	 */
	public ViewBrowser () {

		super();
		setLayout(new BorderLayout());

		componentMap = new HashMap<JComponent, String>();

		/*
		 * Set the SplitPane with the tree tabs on the left and a view panel
		 * on the right.
		 */
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		add(splitPane, BorderLayout.CENTER);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(8);
		splitPane.setContinuousLayout(true);
		
		viewsManager = new CardLayout();
		views = new JPanel(viewsManager);

		viewPanel = new JPanel(new BorderLayout());
		viewPanel.add(views, BorderLayout.CENTER);
		splitPane.setRightComponent(viewPanel);

		viewTitlePanel = new ViewTitle();
		viewPanel.add(viewTitlePanel, BorderLayout.NORTH);
	}


	/**
	 * Sets the position of the view title icon.
	 *
	 * @param  position  the icon position constant
	 */
	public void setTitleIconPosition (int position) {
		viewTitlePanel.setIconPosition(position);
	}


	/**
	 * Gets the currently displayed view component.
	 */
	public JComponent getCurrentView () {
		return (currentViewComponent);
	}


	/**
	 * Adds a view to this ViewBrowser. This simply adds the provided component
	 * to the "views" CardLayout.
	 *
	 */
	public void addView (JComponent view) {

		String name = view.getName();
		componentMap.put(view, name);		
		views.add(view, name);
	}


	/**
	 * Adds a view to this ViewBrowser. This simply adds the provided component
	 * to the "views" CardLayout.
	 *
	 */
	public void addView (JComponent view, String name) {

		componentMap.put(view, name);		
		views.add(view, name);
	}


	/**
	 * Removes a view from this ViewBrowser. 
	 *
	 * @param  view  the view JComponent to remove
	 */
	public void removeView (JComponent view) {

		componentMap.remove(view);		
		views.remove(view);
	}


	/**
	 * Sets the current view. The provided view must have been added to this
	 * ViewBrowser with <code>addView(JComponent view)</code>.
	 *
	 * @param  view  the panel to set as the current view in the right hand pane
	 */
	public void setView (JComponent view) {

		viewsManager.show(views, componentMap.get(view));
		currentViewComponent = view;
	}


	/**
	 * Sets the current view. The provided view must have been added to this
	 * ViewBrowser with <code>addView(JComponent view)</code>.
	 *
	 * @param  view  the panel to set as the current view in the right han pane.
	 */
	public void setView (JComponent view, String title, Icon icon) {

		viewTitlePanel.setTitle(title, icon);
		setView(view);
	}


	/**
	 * Sets the component used for browsing, usually a JTree or JTreeTable.
	 *
	 * @param  browser  the component to be used for browsing
	 */
	public void setBrowser (JComponent browser) {

		this.browser = browser;
		splitPane.setLeftComponent(browser);
	}


	/**
	 * Sets the title on the current view. 
	 *
	 * @param  title
	 * @param  icon
	 */
	public void setTitle (String title, Icon icon) {
		viewTitlePanel.setTitle(title, icon);
	}


	/**
	 * Gets the JLabel component used for the title.
	 *
	 * @return the JLabel for the title component
	 */
	public JLabel getTitleLabel () {
		return (viewTitlePanel.getTitleLabel());
	}


	/**
	 * Gets the JLabel component used for the title.
	 *
	 * @return the JLabel for the title component
	 */
	public JLabel getRightIconLabel () {
		return (viewTitlePanel.getRightIconLabel());
	}


	/**
	 * Gets the JLabel component used for the title.
	 *
	 * @return the JLabel for the title component
	 */
	public JLabel getLeftIconLabel () {
		return (viewTitlePanel.getLeftIconLabel());
	}


	/**
	 * Sets the visibility of the title component.
	 *
	 * @param show  if true will show the title component
	 */
	public void setTitleVisible (boolean show) {

		viewTitlePanel.setVisible(show);

		if (show) {
			viewPanel.add(viewTitlePanel, BorderLayout.NORTH);
		} else {
			viewPanel.remove(viewTitlePanel);
		}

		splitPane.validate();
	}


	/**
	 * Gets the JSplitPane that is used to divide the browser and view.
	 *
	 * @return  the JSplitPane container of the browser and view
	 */
	public JSplitPane getSplitPane () {
		return (splitPane);
	}


	/**
	 * Gets the location of the divider between the browser and view components.
	 *
	 * @return the location of the divider between the browser and view
	 */
	public int getDividerLocation () {
		return (splitPane.getDividerLocation());
	}


	/**
	 * Sets the location of the divider between the browser and view components.
	 *
	 * @param  x  the location of the divider between the browser and view
	 */
	public void setDividerLocation (int x) {
		splitPane.setDividerLocation(x);
	}


	/**
	 * Called when the specified node has been selected in a JTree used
	 * as the browser component.
	 *
	 * @param  node  the node selected in the JTree
	 */
	public void nodeSelected (Object node) {

		if (node instanceof ViewBrowser.TreeNode) {

			ViewBrowser.TreeNode vbn = (ViewBrowser.TreeNode)node;
			String title = vbn.getViewTitle();
			Icon icon = vbn.getViewIcon();
			JComponent view = vbn.getViewComponent();
			setView(view, title, icon);
		}
	}


	/**
	 * Listens for selections on the tree in the browser pane.
	 */
	public class BrowserTreeSelectionListener implements TreeSelectionListener {

		public BrowserTreeSelectionListener () {
		}

		public void valueChanged (TreeSelectionEvent e) {

			Object source = e.getSource();
			if (source instanceof JTree) {

				JTree tree = (JTree)source;
				Object node = tree.getLastSelectedPathComponent();
				nodeSelected(node);
			}
		}
	}


	public static class TreeNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = 1L;
		private View view;


		public TreeNode (View view) {
			this.view = view;
		}

		public JComponent getViewComponent () {
			return (view.getComponent());
		}

		public View getView () {
			return (view);
		}

		public void setView (View view) {
			this.view = view;
		}

		public String getName () {
			return (view.getName());
		}

		public String toString () {
			return (view.getName());
		}

		public Icon getIcon () {
			return (view.getIcon());
		}

		public String getViewTitle () {
			return (view.getTitle());
		}

		public Icon getViewIcon () {
			return (view.getTitleIcon());
		}

		/**
		 * This class implements a Comparator to compare the names of the
		 * TreeNode's View.  Can be used to sort nodes in a JTree.
		 */
		public static class Comparator implements java.util.Comparator<Object> {

			public int compare (Object obj1, Object obj2) {

				TreeNode tn1 = (TreeNode)obj1;
				TreeNode tn2 = (TreeNode)obj2;
				return (tn1.getName().compareTo(tn2.getName()));
			}
		}
	}


	/**
	 * This CellRenderer uses VieBrowser.TreeNode to get information about
	 * how to render the node.
	 */
	public class BrowserTreeCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getTreeCellRendererComponent (
			JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

			if (value instanceof ViewBrowser.TreeNode) {
			
				ViewBrowser.TreeNode node = (ViewBrowser.TreeNode)value;
				value = node.getName();
				Icon icon = node.getIcon();

				if (icon != null) {

					if (leaf) {
	
						setLeafIcon(icon);

					} else if (expanded) {
	
						setOpenIcon(icon);
	
					} else {
	
						setClosedIcon(icon);
					}
				}
			}
	
			return (super.getTreeCellRendererComponent(
				tree, value, selected, expanded, leaf, row, hasFocus));
		}
	}
}
