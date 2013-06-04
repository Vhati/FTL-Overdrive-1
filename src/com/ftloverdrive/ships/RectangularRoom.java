package com.ftloverdrive.ships;

import java.util.Arrays;
import java.util.Collection;

public class RectangularRoom extends Room {
	public final int minX;
	public final int minY;
	public final int maxX;
	public final int maxY;
	private final Tile[] tiles;
	
	public RectangularRoom(int minX, int minY, int maxX, int maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		tiles = new Tile[getWidth() * getHeight()];
		for (int i = 0; i < getWidth(); i++) {
			for (int j = 0; j < getHeight(); j++) {
				tiles[i + j * getWidth()] = new Tile(this, i, j);
			}
		}
	}
	public RectangularRoom(int width, int height) {
		this(0, 0, width - 1, height - 1);
	}

	@Override
	public Collection<Tile> getTiles() {
		return Arrays.asList(tiles);
	}

	@Override
	public Tile tileAt(int x, int y) {
		return tiles[x + y * getWidth()];
	}

	@Override
	public int getMinX() {
		return minX;
	}

	@Override
	public int getMinY() {
		return minY;
	}

	@Override
	public int getMaxX() {
		return maxX;
	}

	@Override
	public int getMaxY() {
		return maxY;
	}
}
