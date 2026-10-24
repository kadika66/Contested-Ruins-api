package xyz.unbent.contestedruins.event;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fired when a reclaim attempt has started on a dormant town: the challenger confirmed, the fee was paid and the
 * guardians are up. The attempt then ends with either a {@link TownReclaimedEvent} or a {@link ReclaimFailedEvent}.
 * Fired on the main thread. Not cancellable.
 */
public class ReclaimStartedEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private final Town town;
	private final Player challenger;
	private final ReclaimType type;
	private final double feePaid;
	@Nullable private final Nation nation;
	@Nullable private final Town challengerTown;
	@Nullable private final String legacyNationName;

	@ApiStatus.Internal
	public ReclaimStartedEvent(@NotNull Town town, @NotNull Player challenger, @NotNull ReclaimType type, double feePaid,
			@Nullable Nation nation, @Nullable Town challengerTown, @Nullable String legacyNationName) {
		this.town = town;
		this.challenger = challenger;
		this.type = type;
		this.feePaid = feePaid;
		this.nation = nation;
		this.challengerTown = challengerTown;
		this.legacyNationName = legacyNationName;
	}

	/** The dormant town being challenged. */
	@NotNull
	public Town getTown() {
		return town;
	}

	/** The player who started the attempt. */
	@NotNull
	public Player getChallenger() {
		return challenger;
	}

	@NotNull
	public ReclaimType getType() {
		return type;
	}

	/** The fee taken from the bank, or 0 when nothing was charged (fee set to 0, or no economy). */
	public double getFeePaid() {
		return feePaid;
	}

	/** {@link ReclaimType#ORDINARY}: the nation the town would join. Null for a legacy reclaim. */
	@Nullable
	public Nation getNation() {
		return nation;
	}

	/** {@link ReclaimType#LEGACY}: the challenger's town, which would become the capital. Null for an ordinary reclaim. */
	@Nullable
	public Town getChallengerTown() {
		return challengerTown;
	}

	/** {@link ReclaimType#LEGACY}: the name of the legacy nation that would be refounded. Null for an ordinary reclaim. */
	@Nullable
	public String getLegacyNationName() {
		return legacyNationName;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
