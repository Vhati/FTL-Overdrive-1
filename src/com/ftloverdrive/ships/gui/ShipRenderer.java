package com.ftloverdrive.ships.gui;

import java.util.HashMap;
import java.util.Map;

import com.ftloverdrive.ships.Room;
import com.ftloverdrive.ships.Ship;
import com.ftloverdrive.ships.Ship.IShipListener;

import de.matthiasmann.twl.Widget;

public class ShipRenderer extends Widget implements IShipListener {
	private final Ship ship;
	private final Map<Room, RoomRenderer> roomRenderers = new HashMap<Room, RoomRenderer>();
	
	public ShipRenderer(Ship ship) {
		this.ship = ship;
		ship.addListener(this);
		for (Room room : ship.getRooms()) {
			onRoomAdded(ship, room);
		}
		onShipRename(ship, "", ship.getName());
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
			roomRenderers.put(room, new RoomRenderer(room));
			add(roomRenderers.get(room));
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		ship.removeListener(this);
	}
}
