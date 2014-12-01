package com.messners.ajf.ui;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetAdapter;

import java.util.HashMap;


/**
 * This class defines a DropTargetListener that rejects all DnD operations
 * except for the specified DataFlavors.
 */
public class DefaultDropTargetListener extends DropTargetAdapter {


	protected DataFlavor acceptFlavor;
	protected HashMap<Component, DropTarget> dropTargetMap = new HashMap<Component, DropTarget>();


	/**
	 * Sets up the specified Component and DropTargetListener for DnD 
	 * handling.
	 */
	public static void addDropTarget (
					Component c, DefaultDropTargetListener ddtl) {
		new DropTarget(c, DnDConstants.ACTION_COPY_OR_MOVE, ddtl);
	}



	/**
	 * Adds the specified Component as a DropTarget.
	 */
	public void addDropTarget (Component c) {

		removeDropTarget(c);
		dropTargetMap.put(c, new DropTarget(
				c, DnDConstants.ACTION_COPY_OR_MOVE, this));
	}


	/**
	 * Removes the specified Component as a DropTarget.
	 */
	public void removeDropTarget (Component c) {

		DropTarget dt = dropTargetMap.get(c);
		if (dt != null) {
			dt.removeDropTargetListener(this);
		}
	}


	/**
	 * Creates an instance that rejects all drag events.
	 */
	public DefaultDropTargetListener () {
	}


	/**
	 * Create an instance to accept drag events from the specified DataFlavor.
	 *
	 * @param acceptFlavor  the DataFlavor to accept drags for
	 */
	public DefaultDropTargetListener (DataFlavor acceptFlavor) {
		this.acceptFlavor = acceptFlavor;
	}


	/**
	 * Get the DataFlavor to accept DnD events for.
	 *
	 * @return the DataFlavor to accept DnD events for
	 */
	public DataFlavor getAcceptFlavor () {
		return (acceptFlavor);
	}


	/**
	 * Set the DataFlavor to accept DnD events for.
	 *
	 * @param acceptFlavor  the DataFlavor to accept DnD events for
	 */
	public void setAcceptFlavor (DataFlavor acceptFlavor) {
		this.acceptFlavor = acceptFlavor;
	}


	public void drop (DropTargetDropEvent e) {

		if (acceptFlavor == null || !e.isDataFlavorSupported(acceptFlavor)) {
			e.rejectDrop();
		}

		e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		doDrop(e);
		e.dropComplete(true);
	}


	public void dragEnter (DropTargetDragEvent e) {

		if (acceptFlavor == null || !e.isDataFlavorSupported(acceptFlavor)) {
			e.rejectDrag();
		}
	}


	/**
	 * This default implementation will simply reject <b>all</b> drops,
	 * override this method to perform the actual drop.  This method will
	 * only be called if the DataFlavor of the drop is supportted by
	 * the current value of acceptFlavor.
	 */
	public void doDrop (DropTargetDropEvent e) {
		e.rejectDrop();
	}
}

