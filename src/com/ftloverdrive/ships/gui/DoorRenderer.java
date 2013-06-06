package com.ftloverdrive.ships.gui;

import com.ftloverdrive.ships.Door;
import com.ftloverdrive.ships.Door.IDoorListener;
import com.ftloverdrive.ships.Tile.TileSide;

import de.matthiasmann.twl.renderer.AnimationState.StateKey;

public class DoorRenderer extends LinkRenderer<Door> implements IDoorListener {
	public DoorRenderer(TileSide tileSide, Door door) {
		super(tileSide, door);
		setTheme("/door");
		door.addListener(this);
	}
	
	@Override
	public void onDoorOpenedOrClosed(Door door, boolean isOpen) {
		if (door == this.link) {
			getAnimationState().setAnimationState(StateKey.get("open"), isOpen);
		}
	}
}
