package xyz.unbent.contestedruins.event;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Fired when a reclaim attempt ends without the town being reclaimed, whatever the reason (see {@link Reason}).
 * The town stays dormant. Fired on the main thread. Not cancellable.
 */
public class ReclaimFailedEvent extends Event {

	/** Why the attempt ended. */
	public enum Reason {
		/** The challenger died. */
		DIED,
		/** The challenger stayed out of the town (or offline) for longer than the away grace. */
		LEFT_TOWN,
		/** The attempt ran out of time. */
		TIMED_OUT,
		/** The challenger gave up with /reclaim abandon. */
		ABANDONED,
		/**
		 * Ended by an admin, by the town being woken or otherwise leaving dormancy, or by the server stopping.
		 * No retry cooldown.
		 */
		CANCELLED,
		/**
		 * The guardians were beaten, but the town couldn't be handed over (for example the challenger's nation no
		 * longer exists, or the legacy nation couldn't be refounded). The fee is refunded.
		 */
		COULD_NOT_COMPLETE
	}

	private static final HandlerList HANDLERS = new HandlerList();

	private final Town town;
	private final UUID challengerId;
	private final String challengerName;
	private final ReclaimType type;
	private final Reason reason;

	@ApiStatus.Internal
	public ReclaimFailedEvent(@NotNull Town town, @NotNull UUID challengerId, @NotNull String challengerName,
			@NotNull ReclaimType type, @NotNull Reason reason) {
		this.town = town;
		this.challengerId = challengerId;
		this.challengerName = challengerName;
		this.type = type;
		this.reason = reason;
	}

	/** The town, still dormant. */
	@NotNull
	public Town getTown() {
		return town;
	}

	@NotNull
	public UUID getChallengerId() {
		return challengerId;
	}

	@NotNull
	public String getChallengerName() {
		return challengerName;
	}

	/** The challenger, or null if they're offline. */
	@Nullable
	public Player getChallenger() {
		return Bukkit.getPlayer(challengerId);
	}

	@NotNull
	public ReclaimType getType() {
		return type;
	}

	@NotNull
	public Reason getReason() {
		return reason;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
