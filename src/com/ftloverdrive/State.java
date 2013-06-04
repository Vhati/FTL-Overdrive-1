package com.ftloverdrive;

import de.matthiasmann.twl.Widget;

public abstract class State extends Widget {
	public void onEnterState() {
		requestKeyboardFocus();
		getOrCreateActionMap().addMapping(this);
	}
	
	public void onLeaveState() {
	}
}