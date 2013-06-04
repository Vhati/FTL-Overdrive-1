package com.ftloverdrive.ships;

public class Tile {
	/**
	 * Room that contains this tile
	 */
	public final Room room;
	
	/**
	 * X coordinate of the tile in room's coordinate system
	 */
	public final int x;
	
	/**
	 * Y coordinate of the tile in room's coordinate system
	 */
	public final int y;
	
	public Tile(Room room, int x, int y) {
		this.room = room;
		this.x = x;
		this.y = y;
	}
}
