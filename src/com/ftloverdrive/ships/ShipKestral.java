package com.ftloverdrive.ships;

import java.util.List;

import com.ftloverdrive.ships.gui.DoorRenderer;
import com.ftloverdrive.ships.gui.LinkRenderer;

public class ShipKestral extends Ship {
	public ShipKestral() {
		super("Kestral");
		addRoom(new RectangularRoom(this, 1, 2).setPosition(14, 2).setTheme("pilot"));
		addRoom(new RectangularRoom(this, 2, 2).setPosition(12, 2));
		addRoom(new RectangularRoom(this, 2, 1).setPosition(10, 2).setTheme("doors"));
		addRoom(new RectangularRoom(this, 2, 1).setPosition(10, 3).setTheme("sensors"));
		addRoom(new RectangularRoom(this, 2, 2).setPosition(8, 1).setTheme("medbay"));
		addRoom(new RectangularRoom(this, 2, 2).setPosition(8, 3).setTheme("shields"));
		addRoom(new RectangularRoom(this, 2, 1).setPosition(6, 0));
		addRoom(new RectangularRoom(this, 2, 2).setPosition(6, 1));
		addRoom(new RectangularRoom(this, 2, 2).setPosition(6, 3));
		addRoom(new RectangularRoom(this, 2, 1).setPosition(6, 5));
		addRoom(new RectangularRoom(this, 2, 2).setPosition(4, 2).setTheme("weapons"));
		addRoom(new RectangularRoom(this, 2, 1).setPosition(3, 1));
		addRoom(new RectangularRoom(this, 2, 1).setPosition(3, 4));
		addRoom(new RectangularRoom(this, 2, 1).setPosition(1, 1).setTheme("oxygen"));
		addRoom(new RectangularRoom(this, 2, 2).setPosition(1, 2).setTheme("engines"));
		addRoom(new RectangularRoom(this, 2, 1).setPosition(1, 4));
		addRoom(new RectangularRoom(this, 1, 2).setPosition(0, 2));

		List<Room> rooms = getRooms();

		//@formatter:off
		new Door(rooms.get( 1).tileAt(1, 1).right,  rooms.get( 0).tileAt(0, 1).left);
		new Door(rooms.get( 2).tileAt(1, 0).right,  rooms.get( 1).tileAt(0, 0).left);
		new Door(rooms.get( 3).tileAt(1, 0).right,  rooms.get( 1).tileAt(0, 1).left);
		new Door(rooms.get( 4).tileAt(1, 1).right,  rooms.get( 2).tileAt(0, 0).left);
		new Door(rooms.get( 5).tileAt(1, 0).right,  rooms.get( 3).tileAt(0, 0).left);
		new Door(rooms.get( 4).tileAt(0, 1).bottom, rooms.get( 5).tileAt(0, 0).top);
		new Door(rooms.get( 7).tileAt(1, 0).right,  rooms.get( 4).tileAt(0, 0).left);
		new Door(rooms.get( 8).tileAt(1, 1).right,  rooms.get( 5).tileAt(0, 1).left);
		new Door(rooms.get( 6).tileAt(1, 0).bottom, rooms.get( 7).tileAt(1, 0).top);
		new Door(rooms.get( 8).tileAt(1, 1).bottom, rooms.get( 9).tileAt(1, 0).top);
		new Door(rooms.get(10).tileAt(1, 0).right,  rooms.get( 7).tileAt(0, 1).left);
		new Door(rooms.get(10).tileAt(1, 1).right,  rooms.get( 8).tileAt(0, 0).left);
		new Door(rooms.get(11).tileAt(1, 0).bottom, rooms.get(10).tileAt(0, 0).top);
		new Door(rooms.get(10).tileAt(0, 1).bottom, rooms.get(12).tileAt(1, 0).top);
		new Door(rooms.get(13).tileAt(1, 0).right,  rooms.get(11).tileAt(0, 0).left);
		new Door(rooms.get(15).tileAt(1, 0).right,  rooms.get(12).tileAt(0, 0).left);
		new Door(rooms.get(13).tileAt(1, 0).bottom, rooms.get(14).tileAt(1, 0).top);
		new Door(rooms.get(14).tileAt(1, 1).bottom, rooms.get(15).tileAt(1, 0).top);
		new Door(rooms.get(16).tileAt(0, 0).right,  rooms.get(14).tileAt(0, 0).left);
		new Door(rooms.get(16).tileAt(0, 1).right,  rooms.get(14).tileAt(0, 1).left);
		//@formatter:on
	}

	static {
		LinkRenderer.registerRenderer(Door.class, DoorRenderer.class);
	}
}
