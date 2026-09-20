# 1.3.1+1.20.1

> ### ⚠️ Read this before updating
>
> This release is a **major technical overhaul and is not backwards compatible.**
>
> - **Requires the matching Spell Engine and More RPG Library releases.** This version will not run
>   on Spell Engine **0.9.x**, and mods built against 0.9.x will not work alongside it.
> - **Update the whole set together.** Spell Engine, More RPG Library and every RPG Series mod must
>   be on matching versions. Mixing in an older add-on will break at startup or misbehave in play.
>
> **Back up your world before updating.**

- Thanks to Daedelus for the PR!
- Ported to Minecraft 1.20.1 (Fabric + Forge 47). NeoForge is replaced by Forge on this line; the same
  Forge jar also loads on NeoForge 1.20.1.
- Requires the matching 1.20.1 releases of Spell Engine (1.10.5), Relics (1.4.0) and
  More RPG Library (2.7.2). Accessory slots come from Trinkets on Fabric and Curios on Forge.
- Every registry write goes through Forge's `RegisterEvent` window, so the mod also boots on
  Forge 47.0-47.3 and on NeoForge 1.20.1, which never unlock the vanilla registries.
- Relic attribute modifiers are now keyed per slot *and* per item, so swapping a relic within one
  accessory slot can no longer trip vanilla's "Modifier is already applied" guard.

### Accepted 1.20.1 limitations

- 1.20.1 has no explosion knockback resistance attribute, so Zhonya's Hourglass and Guardian Angel no
  longer grant it. Melee and projectile knockback is still fully blocked and explosion *damage* is
  still cancelled, but an explosion can now push the player while they are in stasis.
- The three Jewelry figurine recipes stay inert: neither Jewelry nor Additional RPG Jewelry exists on
  the 1.20.1 line yet. The figurines themselves are still registered and obtainable from loot and
  creative.
