package com.ftloverdrive.ships;

import java.util.ArrayList;
import java.util.List;

import com.ftloverdrive.ships.Tile.TileSide;

//TODO create and implement interfaces that allow links to transfer crew, fluids, etc.
public class Door extends Link {
	private boolean isOpen = false;
	
	public Door(TileSide... tileSides) {
		super(tileSides);
	}
	
	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
		fireDoorOpenedOrClosed(isOpen);
	}

	@Override
	public void onMouseClick(TileSide tileSide, int button) {
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
	
	private void fireDoorOpenedOrClosed(boolean isOpen) {
		for (IDoorListener l : listeners) {
			l.onDoorOpenedOrClosed(this, isOpen);
		}
	}
	
	public interface IDoorListener extends ILinkListener {
		public void onDoorOpenedOrClosed(Door door, boolean isOpen);
	}
}
