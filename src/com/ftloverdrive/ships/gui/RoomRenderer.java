package com.ftloverdrive.ships.gui;

import java.util.ArrayList;
import java.util.List;

import com.ftloverdrive.ships.Room;
import com.ftloverdrive.ships.Room.IRoomListener;
import com.ftloverdrive.ships.Tile;

import de.matthiasmann.twl.Widget;

public class RoomRenderer extends Widget implements IRoomListener {
	public final Room room;
	private final List<TileRenderer> tileRenderers = new ArrayList<TileRenderer>();
	
	public RoomRenderer(Room room) {
		this.room = room;
		room.addListener(this);
		onRoomMoved(room);
		onRoomThemeChanged(room, room.getTheme());
		for (Tile tile : room.getTiles()) {
			TileRenderer tr = new TileRenderer(tile);
			tileRenderers.add(tr);
			add(tr);
		}
	}

	@Override
	public void onRoomMoved(Room room) {
		if (room == this.room) {
			invalidateLayout();
		}
	}
	
	@Override
	protected void layout() {
		for (TileRenderer tr : tileRenderers) {
			Tile tile = tr.tile;
			tr.setPosition(
					getX() + (int)(35 * room.getScale() * tile.x),
					getY() + (int)(35 * room.getScale() * tile.y));
			tr.setSize(
					(int)(35 * room.getScale()),
					(int)(35 * room.getScale()));
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void onRoomThemeChanged(Room room, String newTheme) {
		if (room == this.room) {
			setTheme("/room-" + newTheme.toLowerCase());
		}
	}

	@Override
	public void onRoomDestroyed(Room room) {
	}
}
