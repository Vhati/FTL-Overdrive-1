package com.ftloverdrive.ships.gui;

import com.ftloverdrive.ships.Room;
import com.ftloverdrive.ships.Room.IRoomListener;

import de.matthiasmann.twl.Widget;

public class RoomRenderer extends Widget implements IRoomListener {
	public final Room room;
	
	public RoomRenderer(Room room) {
		this.room = room;
		room.addListener(this);
		onRoomMoved(room);
		onRoomThemeChanged(room, room.getTheme());
	}

	@Override
	public void onRoomMoved(Room room) {
		if (room == this.room) {
			invalidateLayout();
		}
	}
	
	@Override
	protected void layout() {
		setPosition(
				getParent().getInnerX() + (int)((room.getX() - room.getScale() / 2) * 35),
				getParent().getInnerY() + (int)((room.getY() - room.getScale() / 2) * 35));
		setSize((int)(room.getWidth() * room.getScale() * 35),
				(int)(room.getHeight() * room.getScale() * 35));
	}

	@Override
	public void onRoomThemeChanged(Room room, String newTheme) {
		if (room == this.room) {
			setTheme("/room-" + newTheme.toLowerCase());
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		room.removeListener(this);
	}
}
