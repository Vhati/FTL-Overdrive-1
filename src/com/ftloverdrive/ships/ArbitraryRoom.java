package com.ftloverdrive.ships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ArbitraryRoom extends Room {
	public final List<Tile> tiles = new ArrayList<Tile>();
	
	/**
	 * This constructor creates a room from an array of strings.
	 * 
	 * <p>All strings must have the same length. Each space is room tile, any other character is a wall.</p>
	 * <p>Keep in mind that default room background works only for rectangular rooms.</p>
	 * 
	 * <p>Example (R-pentamino):</p>
	 * <pre>
	 * <code>
	 * Room room = new ArbitraryRoom(this, "X X",
	 *                                     "   ",
	 *                                     "XX ");
	 * </code>
	 * </pre>
	 * @param ship
	 * @param map Strings that contain room map.
	 */
	public ArbitraryRoom(Ship ship, String... map) {
		super(ship);
		
		int height = map.length;
		if (height == 0) {
			throw new IllegalArgumentException("At least one string expected");
		}
		
		int width = map[0].length();
		if (width == 0) {
			throw new IllegalArgumentException("Strings can't be empty");
		}
		
		for (int i = 0; i < height; i++) {
			if (map[i].length() != width) {
				throw new IllegalArgumentException("All strings should have the same length");
			}
			for (int j = 0; j < width; j++) {
				if (map[i].charAt(j) == ' ') {
					tiles.add(new Tile(this, i, j));
				}
			}
		}
	}

	@Override
	public Collection<Tile> getTiles() {
		return Collections.unmodifiableList(tiles);
	}
}
