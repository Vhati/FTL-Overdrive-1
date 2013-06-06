package com.ftloverdrive.ships;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ship {
	private String name;
	private List<Room> rooms;
	
	public Ship(String name) {
		this.name = name;
		rooms = new ArrayList<Room>();
	}
	
	
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		onShipRename(oldName, newName);
	}
	public String getName() {
		return name;
	}
	
	
	public List<Room> getRooms() {
		return Collections.unmodifiableList(rooms);
	}
	public void addRoom(Room room) {
		rooms.add(room);
		onRoomAdded(room);
	}
	public void removeRoom(Room room) {
		if (!rooms.contains(room)) return;
		rooms.remove(room);
		room.destroy();
		onRoomRemoved(room);
	}

	
	
	
	// Listeners
	private List<IShipListener> listeners = new ArrayList<IShipListener>();
	
	public void addListener(IShipListener l) {
		listeners.add(l);
	}

	public void removeListener(IShipListener l) {
		listeners.remove(l);
	}
	
	private void onShipRename(String oldName, String newName) {
		for (IShipListener l : listeners) {
			l.onShipRename(this, oldName, newName);
		}
	}
	private void onRoomRemoved(Room room) {
		for (IShipListener l : listeners) {
			l.onRoomRemoved(this, room);
		}
	}
	private void onRoomAdded(Room room) {
		for (IShipListener l : listeners) {
			l.onRoomAdded(this, room);
		}
	}
	
	public static interface IShipListener {
		public void onShipRename(Ship ship, String oldName, String newName);
		public void onRoomRemoved(Ship ship, Room room);
		public void onRoomAdded(Ship ship, Room room);
	}
}
