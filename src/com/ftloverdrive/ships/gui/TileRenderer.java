package com.ftloverdrive.ships.gui;

import java.util.ArrayList;
import java.util.List;

import com.ftloverdrive.ships.Link;
import com.ftloverdrive.ships.Tile;
import com.ftloverdrive.ships.Tile.ITileListener;
import com.ftloverdrive.ships.Tile.TileSide;

import de.matthiasmann.twl.Widget;

public class TileRenderer extends Widget implements ITileListener {
	public final Tile tile;
	public final List<LinkRenderer<?>> linkRenderers = new ArrayList<LinkRenderer<?>>();
	
	public TileRenderer(Tile tile) {
		this.tile = tile;
		tile.addListener(this);
		setTheme("/tile");
		for (TileSide side : tile.sides) {
			for (Link link : side.getLinks()) {
				onTileLinkAdded(tile, side, link);
			}
		}
	}

	@Override
	protected void layout() {
		for (LinkRenderer<?> lr : linkRenderers) {
			lr.adjustSize();
			switch (lr.tileSide.side) {
			case LEFT:
				lr.setPosition(
						getX() - lr.getBorderLeft(),
						getY() - lr.getBorderTop() + (getHeight() - lr.getInnerHeight()) / 2);
				break;
			case TOP:
				lr.setPosition(
						getX() - lr.getBorderLeft() + (getWidth() - lr.getInnerWidth()) / 2,
						getY() - lr.getBorderTop());
				break;
			case RIGHT:
				lr.setPosition(
						getRight() - lr.getBorderLeft() - lr.getInnerWidth(),
						getY() - lr.getBorderTop() + (getHeight() - lr.getInnerHeight()) / 2);
				break;
			case BOTTOM:
				lr.setPosition(
						getX() - lr.getBorderLeft() + (getWidth() - lr.getInnerWidth()) / 2,
						getBottom() - lr.getBorderTop() - lr.getInnerHeight());
				break;
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}
	
	@Override
	public void onTileDestroyed(Tile tile) {
	}

	@Override
	public void onTileLinkAdded(Tile tile, TileSide side, Link link) {
		if (tile == this.tile) {
			LinkRenderer<?> lr = LinkRenderer.createRenderer(side, link);
			linkRenderers.add(lr);
			add(lr);
		}
	}

	@Override
	public void onTileLinkRemoved(Tile tile, TileSide side, Link link) {
	}
}
