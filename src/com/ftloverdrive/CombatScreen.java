package com.ftloverdrive;

import com.ftloverdrive.ships.Ship;
import com.ftloverdrive.ships.ShipKestral;
import com.ftloverdrive.ships.gui.ShipRenderer;

import de.matthiasmann.twl.ActionMap.Action;

public class CombatScreen extends State {
	private Ship playerShip;
	private ShipRenderer playerShipRenderer;
	
	public CombatScreen() {
		playerShip = new ShipKestral();
		playerShipRenderer = new ShipRenderer(playerShip);
		add(playerShipRenderer);
	}
	
	@Override
	protected void layout() {
		playerShipRenderer.adjustSize();
		playerShipRenderer.setPosition(
				getInnerX() + (getInnerWidth() - playerShipRenderer.getWidth()) / 2,
				getInnerY() + (getInnerHeight() - playerShipRenderer.getHeight()) / 2);
	}
	
	@Action
	public void back() {
		OverDrive.getInstance().setCurrentState(new MainMenu());
	}
}
