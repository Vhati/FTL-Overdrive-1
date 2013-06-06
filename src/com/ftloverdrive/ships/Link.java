package com.ftloverdrive.ships;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ftloverdrive.ships.Tile.TileSide;

/**
 * Link is a generalization of a door. It connects multiple Tile.TileSide.
 */
public abstract class Link {
	private final TileSide[] tileSides;
	
	public Link(TileSide... tileSides) {
		this.tileSides = tileSides;
		for (TileSide tileSide : tileSides) {
			tileSide.addLink(this);
		}
	}
	
	public List<TileSide> getTileSides() {
		return Collections.unmodifiableList(Arrays.asList(tileSides));
	}
	
	public void destroy() {
		onLinkDestroyed();
	}
	
	// Listeners
	private List<ILinkListener> listeners = new ArrayList<ILinkListener>();
	
	public void addListener(ILinkListener l) {
		listeners.add(l);
	}

	public void removeListener(ILinkListener l) {
		listeners.remove(l);
	}
	
	private void onLinkDestroyed() {
		for (ILinkListener l : listeners) {
			l.onLinkDestroyed(this);
		}
	}
	
	public interface ILinkListener {
		public void onLinkDestroyed(Link link);
	}
}
