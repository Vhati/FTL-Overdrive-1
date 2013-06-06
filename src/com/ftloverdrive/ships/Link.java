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
	private int hoveredTileSides; 
	
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
		fireLinkDestroyed();
	}
	
	/**
	 * Called when player clicks on this link
	 * @param button 0 = LMB, 1 = RMB
	 */
	public void onMouseClick(TileSide tileSide, int button) {
	}
	
	public void onMouseEntered(TileSide tileSide) {
		if (hoveredTileSides == 0) {
			fireSetHover(true);
		}
		hoveredTileSides++;
	}
	
	public void onMouseExited(TileSide tileSide) {
		hoveredTileSides--;
		if (hoveredTileSides == 0) {
			fireSetHover(false);
		}
	}
	
	
	// Listeners
	private List<ILinkListener> listeners = new ArrayList<ILinkListener>();
	
	public void addListener(ILinkListener l) {
		listeners.add(l);
	}

	public void removeListener(ILinkListener l) {
		listeners.remove(l);
	}
	
	private void fireLinkDestroyed() {
		for (ILinkListener l : listeners) {
			l.onLinkDestroyed(this);
		}
	}
	
	private void fireSetHover(boolean hover) {
		for (ILinkListener l : listeners) {
			l.onSetHover(this, hover);
		}
	}
	
	public interface ILinkListener {
		public void onLinkDestroyed(Link link);
		public void onSetHover(Link link, boolean hover);
	}
}
