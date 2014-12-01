package com.messners.ajf.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


/**
 * This abstract class provides an extensible framework used to simplify 
 * construction of preferences dialogs with a navigation pane (outliner 
 * widget on the left side) and a view pane (on the right side).  A good 
 * example of this type of dialog is the Preferences dialog in Netscape 6.
 *
 * @see PrefsPanel
 */
public abstract class PrefsForm extends DialogForm implements Helpable {

	private static final long serialVersionUID = 1L;

	/* Root node which contains PrefsPanels in order. */
	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
	
	/* Components in the dialog. */
	private CardLayout viewPnlLayout = new CardLayout(0, 0);
	private JPanel viewPnl = new JPanel(viewPnlLayout);

	private TreeExt tree = null;
	private JLabel viewPnlLbl = null;
	private JScrollPane treePn = null;

	private HashMap<PrefsPanel, DefaultMutableTreeNode> panels = new HashMap<PrefsPanel, DefaultMutableTreeNode>();
	private boolean treeSizeSet = false;

	private JPanel treePanel;
	private JLabel categoryLabel;

	/* Current preference panel displayed in the view panel. */
	private PrefsPanel curViewPnl = null;

	/* Flag to indicate whether tree nodes are expanded by default. */
	private boolean expandByDefault = true;

	/**
	 * Constructor that sets the expandByDefault flag.
	 *
	 * @param  expandByDefault  the expand tree nodes by default flag
	 */
	public PrefsForm (boolean expandByDefault) {

		super ();
		this.expandByDefault = expandByDefault;
		createUI();
	}


	/**
	 * No args constructor.
	 */
	public PrefsForm () {

		super ();
		createUI();
	}


	/**
	 * Adds a preference panel to the root node in the preference dialog.
	 *
	 * @param pnl Preferences panel to add.  
	 */
	public void addPanel (PrefsPanel pnl) {

		addPanel(null, pnl);
	}

	/**
	 * Adds a preference panel to the specified parent in the preference 
	 * dialog.  After adding preference panels, use buildDialog to 
	 * construct the dialog.
	 *
	 * @param parent the parent PrefsPanel for this panel; if null,
	 *               then panel is added to the root
	 * @param pnl Preferences panel to add.  
	 */
	public void addPanel (PrefsPanel parent, PrefsPanel pnl) {

		viewPnl.add(pnl.getComponent(), pnl.getName());
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(pnl);
		DefaultMutableTreeNode parentNode = (parent != null) ? 
			findNode(parent) : root;
		parentNode.add(child);
		panels.put(pnl, child);
	}

   /**
    * Refreshes the tree, updating the display to reflect any changes.
    *
    * @param pnl Prefrences panel to select, if null then nothing is selected.
    */
   public void refreshTree (PrefsPanel pnl) {

		((DefaultTreeModel) tree.getModel()).reload();
      if (pnl != null) {
         DefaultMutableTreeNode node = findNode(pnl);
         tree.setSelectionPath(new TreePath(node.getPath()));
			processNodeSelection(node);
         if (expandByDefault) {
            tree.expandAllNodes();
         } else {
            tree.collapseAllNodes();
         }
      }
   }

	/**
	 * Removes all preference panels from the dialog.
	 */
	public void removeAllPanels () {

		root.removeAllChildren();
		panels.clear();
		viewPnl.removeAll();
      refreshTree(null);
	}

	/**
	 * Gets the preference panels in depth first order. 
	 * @return Enumeration of preference panels in depth first order.
	 */
	public Enumeration<Object> depthFirstPanels() {
		Vector<Object> pnls = new Vector<Object>();
		DefaultMutableTreeNode node = null;
		for (Enumeration<?> e = root.depthFirstEnumeration();
			     e.hasMoreElements(); ) {
			node = (DefaultMutableTreeNode) e.nextElement();
			if (node != root) {
				pnls.addElement(node.getUserObject());
			}
		}
		return pnls.elements();
	}

	/**
	 * Creates the containing panel and tree for the dialog.
	 */
	protected void createUI () {

		// Create all components used in this dialog.
		categoryLabel = new JLabel("Category");
		categoryLabel.setBorder(BorderFactory.createCompoundBorder
			(BorderFactory.createLineBorder(Color.BLACK),
			 BorderFactory.createEmptyBorder(0, 5, 0, 5)));

		tree = new TreeExt(root);
		tree.putClientProperty("JTree.lineStyle", "None");
		tree.setOpenIcon(ResourceLoader.getIcon(null, "DownTriangle16.gif"));
		tree.setClosedIcon(ResourceLoader.getIcon(null, "RightTriangle16.gif"));
		tree.setLeafIcon(ResourceLoader.getIcon(null, "Blank16.gif"));
		tree.setShowsRootHandles(false);
		tree.setRootVisible(false);
		tree.expandPath(tree.getPathForRow(0));
		tree.getSelectionModel().setSelectionMode
			(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setEditable(false);
		tree.setDragEnabled(false);
		tree.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		treePn = new JScrollPane(tree);
		treePn.setHorizontalScrollBarPolicy
			(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		treePn.setVerticalScrollBarPolicy
			(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		treePanel = new JPanel(new BorderLayout(0, 0));
		treePanel.add(categoryLabel, BorderLayout.NORTH);
		treePanel.add(treePn, BorderLayout.CENTER);

		viewPnlLbl = new JLabel(root.toString());
		viewPnlLbl.setOpaque(true);
		Color viewPnlBG = viewPnlLbl.getBackground();
		viewPnlLbl.setBackground(viewPnlBG.darker()); 
		viewPnlLbl.setForeground(Color.white);
		Font viewPnlLblFnt = viewPnlLbl.getFont();
		viewPnlLbl.setFont(new Font(viewPnlLblFnt.getFontName(),
					    Font.BOLD,
					    viewPnlLblFnt.getSize()));
		viewPnlLbl.setBorder(BorderFactory.createCompoundBorder
			(BorderFactory.createLoweredBevelBorder(),
			 BorderFactory.createEmptyBorder(5, 11, 5, 11)));

		// Layout the dialog.

		setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx = 0;
		cons.gridy = 0;
		cons.gridwidth = 1;
		cons.gridheight = 2;
		cons.weighty = 1.0;
		cons.anchor = GridBagConstraints.NORTHWEST;
		cons.fill = GridBagConstraints.VERTICAL;
		add(treePanel, cons);

		cons.gridx = 1;
		cons.gridy = 0;
		cons.gridwidth = 1;
		cons.gridheight = 1;
		cons.insets.left = 0;
		cons.weightx = 1.0;
		cons.weighty = 0.0;
		cons.anchor = GridBagConstraints.NORTHWEST;
		cons.fill = GridBagConstraints.HORIZONTAL;
		add(viewPnlLbl, cons);

		cons.gridx = 1;
		cons.gridy = 1;
		cons.gridwidth = 1;
		cons.gridheight = 1;
		cons.weighty = 1.0;
		cons.anchor = GridBagConstraints.NORTHWEST;
		cons.fill = GridBagConstraints.BOTH;
		cons.insets.left   = 11;
		cons.insets.right  = 0;
		cons.insets.top    = 11;
		cons.insets.bottom = 0;
		add(viewPnl, cons);


		// Register event listeners.
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				tree_valueChanged(e);
			}
		});
	}

	/**
	 * Hides/shows the title above the tree on the left hand side.
	 *
	 * @param  flag the visibility flag
	 */
	public void setTreeTitleVisible (boolean flag) {

		if (flag) {
			treePanel.add(categoryLabel, BorderLayout.NORTH);
		} else {
			treePanel.remove(categoryLabel);
		}

		treePanel.invalidate();
	}


	public int popup () {
	   
		return (popup(null));
	}


	public int popup (Object parent) {

		// Exapnd all nodes and select the first child in the tree by default.
		initializeTree();
		return (super.popup(parent));
	}


	protected void initializeTree () {

		if (root.getChildCount() > 0) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				root.getFirstChild();
			tree.setSelectionPath(new TreePath(node.getPath()));
			processNodeSelection(node);

			if (expandByDefault) {
				tree.expandAllNodes();
			} else {
				tree.collapseAllNodes();
			}
		}

		setTreeSize();
	}

	/**
	 * Sets the tree scroll pane width so that it's wide enough to just
	 * accomodate all expanded tree nodes without horizontal scroll bars.
	 */
	protected void setTreeSize () {

		/*
		 * Only need to do this once
		 */
		if (treeSizeSet) {
			return;
		}

		int totalPanelCount = panels.size();
		tree.setVisibleRowCount((totalPanelCount < 30 ? totalPanelCount : 30));

		Dimension size = tree.getPreferredScrollableViewportSize();
		JComponent viewport = treePn.getViewport();
		viewport.setPreferredSize(size);

		if (size.height > 200) {
			size.height = 200;
		}

		viewport.setMinimumSize(size);
		treeSizeSet = true;
	}
	
	// From Helpable
	public void help(Object helpOn) {
	   
		if (curViewPnl instanceof Helpable) {
			Helpable curViewHlp = (Helpable) curViewPnl;
			curViewHlp.help(helpOn);
		}
	}

	/**
	 * Event handler for tree selection events.
	 * @param event Tree selection event.
	 */
	private void tree_valueChanged(TreeSelectionEvent event) {
	   
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) 
			tree.getLastSelectedPathComponent();
		if (node != null) {
			processNodeSelection(node);
		}
	}

	/**
	 * Processes node selection.
	 * @param node The selected node.
	 */
	private void processNodeSelection (DefaultMutableTreeNode node) {

		boolean okToLeave = (curViewPnl == null) ? true : curViewPnl.leave();
		if (okToLeave) {

			curViewPnl = (PrefsPanel)node.getUserObject();
			curViewPnl.enter();

			viewPnlLbl.setIcon(curViewPnl.getIcon());
			viewPnlLbl.setText(curViewPnl.getTitle());

			viewPnlLayout.show(viewPnl, curViewPnl.getName());
			setHelpButtonEnabled(curViewPnl instanceof Helpable);
		}
	}

	/**
	 * Finds the node which contains the PrefsPanel.
	 * @param name Unique identifier for panel to look for.
	 * @return Node that contains the specified panel.
	 */
	private DefaultMutableTreeNode findNode (PrefsPanel panel) {

		return panels.get(panel);
	}
}
