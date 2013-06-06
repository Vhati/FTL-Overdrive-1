package com.ftloverdrive.ships.gui;

import java.util.HashMap;
import java.util.Map;

import com.ftloverdrive.ships.Room;
import com.ftloverdrive.ships.Ship;
import com.ftloverdrive.ships.Ship.IShipListener;

import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.AnimationState.StateKey;

public class ShipRenderer extends Widget implements IShipListener {
	private final Ship ship;
	private final Map<Room, RoomRenderer> roomRenderers = new HashMap<Room, RoomRenderer>();
	private boolean showRooms;
	
	public ShipRenderer(Ship ship) {
		this.ship = ship;
		ship.addListener(this);
		for (Room room : ship.getRooms()) {
			onRoomAdded(ship, room);
		}
		onShipRename(ship, "", ship.getName());
		setShowRooms(true);
	}

	public void setShowRooms(boolean showRooms) {
		this.showRooms = showRooms;
		for (RoomRenderer roomRenderer : roomRenderers.values()) {
			roomRenderer.setVisible(showRooms);
		}
		getAnimationState().setAnimationState(StateKey.get("showRooms"), showRooms);
	}
	
	public boolean getShowRooms() {
		return showRooms;
	}
	
	@Override
	protected void layout() {
		for (RoomRenderer rr : roomRenderers.values()) {
			Room room = rr.room;
			rr.setPosition(
					getInnerX() + (int)(room.getX() * 35),
					getInnerY() + (int)(room.getY() * 35));
			rr.setSize(
					(int)(room.getWidth() * room.getScale() * 35),
					(int)(room.getHeight() * room.getScale() * 35));
		}
	}
	
	@Override
	public void onShipRename(Ship ship, String oldName, String newName) {
		if (ship == this.ship) {
			setTheme("/ship-" + newName.toLowerCase());
		}
	}

	@Override
	public void onRoomRemoved(Ship ship, Room room) {
		if (ship == this.ship) {
			removeChild(roomRenderers.get(room));
			roomRenderers.remove(room);
		}
	}

	@Override
	public void onRoomAdded(Ship ship, Room room) {
		if (ship == this.ship && !roomRenderers.containsKey(room)) {
			RoomRenderer rr = new RoomRenderer(room);
			roomRenderers.put(room, rr);
			add(rr);
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}
}
