package com.ftloverdrive.ships;

public interface IClickableLink {
	/**
	 * Called when player clicks on this link
	 * @param button 0 = LMB, 1 = RMB
	 */
	public void onClick(int button);
}
