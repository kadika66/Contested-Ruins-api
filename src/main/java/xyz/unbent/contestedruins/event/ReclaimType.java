package xyz.unbent.contestedruins.event;

/** The two ways a dormant town can be reclaimed. */
public enum ReclaimType {
	/** A nation wins the town, and the town joins that nation. */
	ORDINARY,
	/**
	 * At the fallen capital of a legacy nation, the mayor of an independent town wins it, and the legacy nation is
	 * refounded with their town as its capital and the old capital as a member.
	 */
	LEGACY
}
