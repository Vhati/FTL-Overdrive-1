package com.ftloverdrive.ships.gui;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.ftloverdrive.ships.Direction;
import com.ftloverdrive.ships.IClickableLink;
import com.ftloverdrive.ships.Link;
import com.ftloverdrive.ships.Link.ILinkListener;
import com.ftloverdrive.ships.Tile;
import com.ftloverdrive.ships.Tile.TileSide;

import de.matthiasmann.twl.Event;
import static de.matthiasmann.twl.Event.Type.*;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.AnimationState.StateKey;

public abstract class LinkRenderer<T extends Link> extends Widget implements ILinkListener {
	private static Map<Class<? extends Link>, Class<? extends LinkRenderer<?>>> rendererClasses =
			new HashMap<Class<? extends Link>, Class<? extends LinkRenderer<?>>>();
	
	/**
	 * Registers a renderer class. Instances of rendererClass will be used to render links that are instances of linkClass.
	 * rendererClass must have a constructor that accepts a TileSide and a linkClass. It can't have other parameters.
	 * Example:
	 * <code>
	 * public class DoorRenderer extends LinkRenderer<Door> {
	 *     public DoorRenderer(TileSide side, Door door) {
	 *         //...
	 *     }
	 *     // ...
	 * }
	 * //...
	 * //...
	 * LinkRenderer.registerRenderer(Door.class, DoorRenderer.class);
	 * </code> 
	 * @param linkClass
	 * @param rendererClass
	 */
	public static void registerRenderer(Class<? extends Link> linkClass, Class<? extends LinkRenderer<?>> rendererClass) {
		rendererClasses.put(linkClass, rendererClass);
	}
	
	/**
	 * Factory that creates a renderer for given link.
	 * @param link
	 * @return
	 */
	public static LinkRenderer<?> createRenderer(TileSide side, Link link) {
		try {
			Class<? extends LinkRenderer<?>> c = rendererClasses.get(link.getClass());
			Constructor<? extends LinkRenderer<?>> constructor = c.getConstructor(TileSide.class, link.getClass());
			return constructor.newInstance(side, link);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create a renderer for link " + link + " of type " + link.getClass(), e);
		}
	}
	
	public final Tile tile;
	public final TileSide tileSide;
	public final T link;
	
	protected LinkRenderer(TileSide tileSide, T link) {
		this.tile = tileSide.tile;
		this.tileSide = tileSide;
		this.link = link;
		link.addListener(this);
		getAnimationState().setAnimationState(StateKey.get("left"), tileSide.side == Direction.LEFT);
		getAnimationState().setAnimationState(StateKey.get("top"), tileSide.side == Direction.TOP);
		getAnimationState().setAnimationState(StateKey.get("right"), tileSide.side == Direction.RIGHT);
		getAnimationState().setAnimationState(StateKey.get("bottom"), tileSide.side == Direction.BOTTOM);
	}
	
	@Override
	public int getPreferredWidth() {
		if (tileSide.side == Direction.LEFT || tileSide.side == Direction.RIGHT)
			return Math.min(super.getPreferredWidth(), super.getPreferredHeight());
		else
			return Math.max(super.getPreferredWidth(), super.getPreferredHeight());
	}
	
	@Override
	public int getPreferredHeight() {
		if (tileSide.side == Direction.LEFT || tileSide.side == Direction.RIGHT)
			return Math.max(super.getPreferredWidth(), super.getPreferredHeight());
		else
			return Math.min(super.getPreferredWidth(), super.getPreferredHeight());
	}
	
    @Override
    protected boolean handleEvent(Event evt) {
        if (link instanceof IClickableLink) {
        	if (evt.getType() == MOUSE_BTNDOWN) {
            	((IClickableLink) link).onClick(evt.getMouseButton());
            }
        	return evt.isMouseEvent() && evt.getType() != MOUSE_WHEEL;
        }
		return false;
    }

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void onLinkDestroyed(Link link) {
		if (link == this.link && getParent() != null) {
			getParent().removeChild(this);
		}
	}
}
