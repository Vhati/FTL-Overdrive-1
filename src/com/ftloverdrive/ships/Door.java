package com.ftloverdrive.ships;

import java.util.ArrayList;
import java.util.List;

import com.ftloverdrive.ships.Tile.TileSide;

//TODO create and implement interfaces that allow links to transfer crew, fluids, etc.
public class Door extends Link implements IClickableLink {
	private boolean isOpen = false;
	
	public Door(TileSide... tileSides) {
		super(tileSides);
	}
	
	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
		onDoorOpenedOrClosed(isOpen);
	}

	@Override
	public void onClick(int button) {
		setOpen(!isOpen());
	}
	

	// Listeners
	private List<IDoorListener> listeners = new ArrayList<IDoorListener>();
	
	public void addListener(IDoorListener l) {
		listeners.add(l);
	}

	public void removeListener(IDoorListener l) {
		listeners.remove(l);
	}
	
	private void onDoorOpenedOrClosed(boolean isOpen) {
		for (IDoorListener l : listeners) {
			l.onDoorOpenedOrClosed(this, isOpen);
		}
	}
	
	public interface IDoorListener extends ILinkListener {
		public void onDoorOpenedOrClosed(Door door, boolean isOpen);
	}
}
