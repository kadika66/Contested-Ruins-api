package xyz.unbent.contestedruins;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Entry point for other plugins.
 *
 * <pre>{@code
 * if (ContestedRuinsAPI.isDormant(town)) {
 *     // the town has fallen and is held by the corruption
 * }
 * }</pre>
 *
 * Every method is safe to call when Contested Ruins isn't running: it answers {@code false} (or 0).
 * {@link #isAvailable()} says whether it is. To react to towns falling and being reclaimed, listen for the events in
 * {@link xyz.unbent.contestedruins.event}. Call these from the main server thread.
 *
 * A plugin using this should list {@code ContestedRuins} under {@code softdepend} (or {@code depend}) in its
 * plugin.yml, so Contested Ruins is enabled first, and depend on {@code xyz.unbent:contestedruins-api} with
 * scope {@code provided}: the Contested Ruins plugin jar carries these classes at runtime.
 */
public final class ContestedRuinsAPI {

	private static volatile Provider provider;

	private ContestedRuinsAPI() {}

	/** True while Contested Ruins is enabled and answering. */
	public static boolean isAvailable() {
		return provider != null;
	}

	/** True if the town has fallen to the corruption: no residents, held by an NPC steward, waiting to be reclaimed. */
	public static boolean isDormant(@NotNull Town town) {
		Provider p = provider;
		return p != null && p.isDormant(town);
	}

	/** True if the entity is one of Contested Ruins' champions, minions or wave mobs. */
	public static boolean isCorruptionMob(@NotNull Entity entity) {
		Provider p = provider;
		return p != null && p.isCorruptionMob(entity);
	}

	/** True if the nation is in the legacy nations registry. */
	public static boolean isLegacyNation(@NotNull Nation nation) {
		Provider p = provider;
		return p != null && p.isLegacyNation(nation);
	}

	/**
	 * A dormant town's corruption strength, which scales its guardians: bigger towns are stronger.
	 *
	 * @return the strength, or 0 if the town isn't dormant (or Contested Ruins isn't running).
	 */
	public static int getCorruptionStrength(@NotNull Town town) {
		Provider p = provider;
		return p == null ? 0 : p.getCorruptionStrength(town);
	}

	/** True while a reclaim attempt is running on the town. */
	public static boolean isReclaimInProgress(@NotNull Town town) {
		Provider p = provider;
		return p != null && p.isReclaimInProgress(town);
	}

	/** Implemented by the Contested Ruins plugin. Not for other plugins. */
	@ApiStatus.Internal
	public interface Provider {
		boolean isDormant(@NotNull Town town);

		boolean isCorruptionMob(@NotNull Entity entity);

		boolean isLegacyNation(@NotNull Nation nation);

		int getCorruptionStrength(@NotNull Town town);

		boolean isReclaimInProgress(@NotNull Town town);
	}

	/** Set by the Contested Ruins plugin itself when it enables, and cleared when it disables. Not for other plugins. */
	@ApiStatus.Internal
	public static void setProvider(@Nullable Provider newProvider) {
		provider = newProvider;
	}
}
