# Contested Ruins API

The public API for **Contested Ruins**, an addon for [Towny](https://github.com/TownyAdvanced/Towny) where bankrupt towns
fall to the corruption and must be won back in combat: slay the champion holding the town, or clear the waves rising
across its streets.

Use this API to check whether a town has fallen, whether a mob is one of its guardians, and to react when towns fall,
reclaims start, and towns are won back. Contested Ruins itself is *[sold on BuiltByBit: link]*; this API is MIT licensed.

## Add it to your project

Through [JitPack](https://jitpack.io/#kadika66/Contested-Ruins-api). Always `provided`: the Contested Ruins plugin
carries these classes at runtime, so never shade them.

**Maven**
```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>

<dependency>
  <groupId>com.github.kadika66</groupId>
  <artifactId>Contested-Ruins-api</artifactId>
  <version>1.0.0</version>
  <scope>provided</scope>
</dependency>
```

**Gradle (Kotlin)**
```kotlin
repositories { maven("https://jitpack.io") }
dependencies { compileOnly("com.github.kadika66:Contested-Ruins-api:1.0.0") }
```

You'll also need `paper-api` and `towny` as provided dependencies (the API mentions Towny's `Town` and `Nation`).

And in your `plugin.yml`, so Contested Ruins enables first:
```yaml
softdepend: [ContestedRuins]
```

## Checks

On `xyz.unbent.contestedruins.ContestedRuinsAPI`. Every method is safe to call when Contested Ruins isn't installed or
running: it answers `false` (or `0`). Call them from the server thread.

| Method | Answers |
|---|---|
| `isAvailable()` | Contested Ruins is enabled |
| `isDormant(Town)` | The town has fallen and is held by the corruption |
| `isCorruptionMob(Entity)` | The entity is a champion, minion or wave mob |
| `isLegacyNation(Nation)` | The nation is in the legacy nations registry |
| `getCorruptionStrength(Town)` | A dormant town's strength, which scales its guardians (0 if not dormant) |
| `isReclaimInProgress(Town)` | A reclaim attempt is running on the town |

```java
if (ContestedRuinsAPI.isDormant(town)) {
    // the town has fallen: no residents, held by an NPC steward, waiting to be reclaimed
}
```

## Events

Package `xyz.unbent.contestedruins.event`. All fire on the server thread and can't be cancelled.

| Event | When |
|---|---|
| `TownFellDormantEvent` | A town has gone dormant. Clean up per-town state here (guards, shops, claims) |
| `ReclaimStartedEvent` | An attempt started: the challenger confirmed, the fee was paid, the guardians are up |
| `TownReclaimedEvent` | A town was won back: it belongs to `getNation()`, and any victory reward has been paid |
| `ReclaimFailedEvent` | An attempt ended without a reclaim; `getReason()` says why |

Every attempt fires one `ReclaimStartedEvent` and then exactly one of `TownReclaimedEvent` or `ReclaimFailedEvent`,
unless the town itself is deleted mid-attempt.

`ReclaimType` tells the two kinds of reclaim apart: `ORDINARY` (a nation wins the town, and it joins that nation) or
`LEGACY` (at a fallen legacy nation's capital, an independent town's mayor wins it and the legacy nation is refounded).

`ReclaimFailedEvent.Reason`: `DIED`, `LEFT_TOWN`, `TIMED_OUT`, `ABANDONED`, `CANCELLED` (an admin, the town leaving
dormancy, or the server stopping), `COULD_NOT_COMPLETE` (the guardians were beaten but the town couldn't be handed over;
the fee is refunded).

```java
@EventHandler
public void onReclaimed(TownReclaimedEvent event) {
    Town town = event.getTown();
    Nation nation = event.getNation();
    double reward = event.getVictoryReward();   // paid into a bank, 0 if none
    // e.g. give the new owners a starter garrison
}

@EventHandler
public void onFell(TownFellDormantEvent event) {
    Town town = event.getTown();
    // drop anything your plugin keeps for this town
}
```

## Example: Garrison

[Garrison](https://github.com/kadika66/garrison-api) uses this API: a town that falls loses its garrison
(`TownFellDormantEvent`), guards treat corruption mobs as hostile and earn training XP for them (`isCorruptionMob`),
and a dormant town can't hire guards (`isDormant`).

## Versioning

Additions raise the minor number; anything a plugin relies on is only removed or changed in a new major version.

## Documentation

The [wiki](https://github.com/kadika66/Contested-Ruins-api/wiki) covers Contested Ruins for server owners and developers.

## Support

Questions and bug reports: the [plugin support channel on Discord](https://discord.gg/ACMTFTz7Sq), or open an issue here.

## Licence

MIT, see [LICENSE](LICENSE). This covers the API only; the Contested Ruins plugin is proprietary.
