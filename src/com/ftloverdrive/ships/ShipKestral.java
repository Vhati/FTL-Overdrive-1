package com.ftloverdrive.ships;

public class ShipKestral extends Ship {
	public ShipKestral() {
		super("Kestral");
		addRoom(new RectangularRoom(1, 2).setPosition(14, 2).setTheme("pilot"));
		addRoom(new RectangularRoom(2, 2).setPosition(12, 2));
		addRoom(new RectangularRoom(2, 1).setPosition(10, 2).setTheme("doors"));
		addRoom(new RectangularRoom(2, 1).setPosition(10, 3).setTheme("sensors"));
		addRoom(new RectangularRoom(2, 2).setPosition(8, 1).setTheme("medbay"));
		addRoom(new RectangularRoom(2, 2).setPosition(8, 3).setTheme("shields"));
		addRoom(new RectangularRoom(2, 1).setPosition(6, 0));
		addRoom(new RectangularRoom(2, 2).setPosition(6, 1));
		addRoom(new RectangularRoom(2, 2).setPosition(6, 3));
		addRoom(new RectangularRoom(2, 1).setPosition(6, 5));
		addRoom(new RectangularRoom(2, 2).setPosition(4, 2).setTheme("weapons"));
		addRoom(new RectangularRoom(2, 1).setPosition(3, 1));
		addRoom(new RectangularRoom(2, 1).setPosition(3, 4));
		addRoom(new RectangularRoom(2, 1).setPosition(1, 1).setTheme("oxygen"));
		addRoom(new RectangularRoom(2, 2).setPosition(1, 2).setTheme("engines"));
		addRoom(new RectangularRoom(2, 1).setPosition(1, 4));
		addRoom(new RectangularRoom(1, 2).setPosition(0, 2));
	}
}
