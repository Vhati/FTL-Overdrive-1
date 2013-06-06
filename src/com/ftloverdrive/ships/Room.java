package com.ftloverdrive.ships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Room {
	public final Ship ship;
	
	private String theme = "empty";

	private double x = 0;
	private double y = 0;
	private double scale = 1;
	
	public Room(Ship ship) {
		this.ship = ship;
	}

	/**
	 * Finds all tiles in this room. Room must contain at least 1 tile, and can't be infinite.
	 * @return Collection containing all tiles
	 */
	public abstract Collection<Tile> getTiles();

	/**
	 * @return X coordinate of room's origin in ship's coordinate system
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * @return Y coordinate of room's origin in ship's coordinate system
	 */
	public double getY() {
		return y;
	}

	/**
	 * Moves the room
	 * @param x New X coordinate of room's origin in ship's coordinate system
	 * @param y New Y coordinate of room's origin in ship's coordinate system
	 * @return this
	 */
	public Room setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		onRoomMoved();
		return this;
	}
	
	/**
	 * @return Scale of this room (size of each tile)
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Scales the room
	 * @param rotation New scale of this room (size of each tile)
	 * @return this
	 */
	public Room setScale(double scale) {
		this.scale = scale;
		onRoomMoved();
		return this;
	}
	
	/**
	 * Gets the room theme. It defines things like background.
	 * @return Current theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * Sets the room theme. It defines things like background.
	 * @param theme New theme
	 * @return this
	 */
	public Room setTheme(String theme) {
		this.theme = theme;
		onRoomThemeChanged(theme);
		return this;
	}

	/**
	 * Finds a tile such that (tile.x == x && tile.y == y && getTiles().contains(tile))
	 * @param x
	 * @param y
	 * @return Tile at (x, y) or null if there is no tile there
	 */
	public Tile tileAt(int x, int y) {
		for (Tile t : getTiles()) {
			if (t.x == x && t.y == y) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * @return X coordinate of the leftmost tile.
	 */
	public int getMinX() {
		int res = Integer.MAX_VALUE;
		for (Tile t : getTiles()) {
			res = Math.min(res, t.x);
		}
		return res;
	}
	
	/**
	 * @return Y coordinate of the topmost tile.
	 */
	public int getMinY() {
		int res = Integer.MAX_VALUE;
		for (Tile t : getTiles()) {
			res = Math.min(res, t.y);
		}
		return res;
	}
	
	/**
	 * @return X coordinate of the rightmost tile.
	 */
	public int getMaxX() {
		int res = Integer.MIN_VALUE;
		for (Tile t : getTiles()) {
			res = Math.max(res, t.x);
		}
		return res;
	}
	
	/**
	 * @return Y coordinate of the bottommost tile.
	 */
	public int getMaxY() {
		int res = Integer.MIN_VALUE;
		for (Tile t : getTiles()) {
			res = Math.max(res, t.y);
		}
		return res;
	}
	
	public int getWidth() {
		return getMaxX() - getMinX() + 1;
	}
	
	public int getHeight() {
		return getMaxY() - getMinY() + 1;
	}

	public void destroy() {
		for (Tile tile : getTiles()) {
			tile.destroy();
		}
		if (ship.getRooms().contains(this)) {
			ship.removeRoom(this);
		}
		onRoomDestroyed();
	}
	
	// Listeners
	private List<IRoomListener> listeners = new ArrayList<IRoomListener>();
	
	public void addListener(IRoomListener l) {
		listeners.add(l);
	}

	public void removeListener(IRoomListener l) {
		listeners.remove(l);
	}
	
	private void onRoomMoved() {
		for (IRoomListener l : listeners) {
			l.onRoomMoved(this);
		}
	}
	
	private void onRoomThemeChanged(String newTheme) {
		for (IRoomListener l : listeners) {
			l.onRoomThemeChanged(this, newTheme);
		}
	}
	
	private void onRoomDestroyed() {
		for (IRoomListener l : listeners) {
			l.onRoomDestroyed(this);
		}
	}
	
	public static interface IRoomListener {
		public void onRoomMoved(Room room);
		public void onRoomThemeChanged(Room room, String newTheme);
		public void onRoomDestroyed(Room room);
	}
}
