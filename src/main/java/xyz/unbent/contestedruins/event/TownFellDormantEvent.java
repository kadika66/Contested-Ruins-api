package xyz.unbent.contestedruins.event;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fired after a town has gone dormant: its residents are gone, an NPC mayor holds it,
 * and the corruption has taken it. Other plugins listen for this to clean up their own
 * per-town state (town guards, shops, claims on the town's land).
 */
public class TownFellDormantEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private final Town town;
	private final int infestation;
	private final String formerNation;
	private final String fallenLegacyNation;

	public TownFellDormantEvent(Town town, int infestation, @Nullable String formerNation, @Nullable String fallenLegacyNation) {
		this.town = town;
		this.infestation = infestation;
		this.formerNation = formerNation;
		this.fallenLegacyNation = fallenLegacyNation;
	}

	public Town getTown() {
		return town;
	}

	/** The corruption strength the town fell with. */
	public int getInfestation() {
		return infestation;
	}

	/** The nation the town belonged to when it fell, or null. */
	@Nullable
	public String getFormerNation() {
		return formerNation;
	}

	/** If the town was a legacy nation's capital, that nation's name, otherwise null. */
	@Nullable
	public String getFallenLegacyNation() {
		return fallenLegacyNation;
	}

	/** @deprecated Renamed in 0.2.0. Use {@link #getFallenLegacyNation()}. */
	@Deprecated
	@Nullable
	public String getFallenFoundingNation() {
		return fallenLegacyNation;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
