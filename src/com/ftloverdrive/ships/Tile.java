package com.ftloverdrive.ships;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ftloverdrive.ships.Link.ILinkListener;

import static com.ftloverdrive.ships.Direction.*;

public class Tile {
	public class TileSide implements ILinkListener {
		public final Tile tile;
		public final Direction side;
		private final List<Link> links = new ArrayList<Link>();
		
		public TileSide(Direction side) {
			tile = Tile.this;
			this.side = side;
		}

		/**
		 * Don't call this directly. Should only be called by Link constructor
		 */
		void addLink(Link link) {
			links.add(link);
			fireTileLinkAdded(this, link);
		}
		
		@Override
		public void onLinkDestroyed(Link link) {
			if (links.contains(link)) {
				links.remove(link);
			}
			fireTileLinkRemoved(this, link);
		}
		
		/**
		 * @return All links in this tile side
		 */
		public List<Link> getLinks() {
			return Collections.unmodifiableList(links);
		}

		public void destroy() {
			for (Link link : links) {
				link.removeListener(this);
				link.destroy();
			}
		}

		@Override
		public void onSetHover(Link link, boolean hover) {
		}
	}
	
	public final TileSide left = new TileSide(LEFT);
	public final TileSide top = new TileSide(TOP);
	public final TileSide right = new TileSide(RIGHT);
	public final TileSide bottom = new TileSide(BOTTOM);
	public final TileSide[] sides = new TileSide[4];
	
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
		sides[LEFT.ordinal()] = left;
		sides[TOP.ordinal()] = top;
		sides[RIGHT.ordinal()] = right;
		sides[BOTTOM.ordinal()] = bottom;
		this.room = room;
		this.x = x;
		this.y = y;
	}

	public void destroy() {
		for (TileSide side : sides) {
			side.destroy();
		}
		fireTileDestroyed();
	}
	
	// Listeners
	private List<ITileListener> listeners = new ArrayList<ITileListener>();
	
	public void addListener(ITileListener l) {
		listeners.add(l);
	}

	public void removeListener(ITileListener l) {
		listeners.remove(l);
	}
	
	private void fireTileDestroyed() {
		for (ITileListener l : listeners) {
			l.onTileDestroyed(this);
		}
	}
	
	private void fireTileLinkAdded(TileSide side, Link link) {
		for (ITileListener l : listeners) {
			l.onTileLinkAdded(this, side, link);
		}
	}
	
	private void fireTileLinkRemoved(TileSide side, Link link) {
		for (ITileListener l : listeners) {
			l.onTileLinkRemoved(this, side, link);
		}
	}
	
	public static interface ITileListener {
		public void onTileDestroyed(Tile tile);
		public void onTileLinkAdded(Tile tile, TileSide side, Link link);
		public void onTileLinkRemoved(Tile tile, TileSide side, Link link);
	}
}
