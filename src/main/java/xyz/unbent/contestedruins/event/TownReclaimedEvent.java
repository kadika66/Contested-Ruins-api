package xyz.unbent.contestedruins.event;

import com.palmergames.bukkit.towny.object.Nation;
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
 * Fired after a dormant town has been won back: it is no longer dormant, it belongs to {@link #getNation()}, and any
 * victory reward has been paid. The town is still run by its NPC steward until the nation leader appoints a governor.
 * Fired on the main thread. Not cancellable.
 */
public class TownReclaimedEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private final Town town;
	private final UUID challengerId;
	private final String challengerName;
	private final ReclaimType type;
	private final Nation nation;
	private final double victoryReward;

	@ApiStatus.Internal
	public TownReclaimedEvent(@NotNull Town town, @NotNull UUID challengerId, @NotNull String challengerName,
			@NotNull ReclaimType type, @NotNull Nation nation, double victoryReward) {
		this.town = town;
		this.challengerId = challengerId;
		this.challengerName = challengerName;
		this.type = type;
		this.nation = nation;
		this.victoryReward = victoryReward;
	}

	/** The town that was won back. */
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

	/**
	 * The nation the town now belongs to: the challenger's nation ({@link ReclaimType#ORDINARY}), or the refounded
	 * legacy nation ({@link ReclaimType#LEGACY}).
	 */
	@NotNull
	public Nation getNation() {
		return nation;
	}

	/**
	 * What was paid into a bank as the victory reward: the town's (ordinary) or the refounded nation's (legacy).
	 * 0 when the reward is off or no fee was charged.
	 */
	public double getVictoryReward() {
		return victoryReward;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
