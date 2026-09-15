# 1.3.1+1.20.1

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

# 1.3.1 - 1.21.1
- Drop Forgified Fabric API (FFAPI) as a required dependency

# 1.3.0 - 1.21.1
- Adopt Spell Engine 1.10 - Thanks Daedelus for the PR!
- You cant get Buffs or Heals while the Zhonyas Hourglass and the Guardian Angle Effect are active
- The Sunfire Cape Effect was changed to a ticking Stash Effect (Dealing Damage every second) 

# 1.2.1 - 1.21.1
- Fixed a crash due to a missing reference caused by a new MRPG-Lib Update

# 1.2.0 - 1.21.1
- Spell Engine 1.9 API Update
- Guardian Angel and Zhonyas Hourglass now have their own Action Impairing Hud Message
- When Zhonyas Hourglass is used, the player model is now rendered Gold
- Fixed some wrong Buff Particle Id's
- Fixed the wrong Id for the Guardian Angel Sound

# 1.1.1 - 1.21.1
- Add Nature Orb for the new Nature Spell Power
- Change Jewelry Figurines to fit the new Gems from Additional Jewelry Mod
- change lesser proc relics to match the style of the original Relic Mod's Relics
- this was done to reduce unnecessary content and reduce the amount relic items a bit
- fix relics not working on neoforge

# 1.1.0 - 1.21.1
- NeoForge Beta!
- Fix crash with Spell Engine 1.8.3

# 1.0.2 - 1.21.1
- Spell Engine 1.7

# 1.0.1 - 1.21.1
- Update License
- fix Liandry's Torment inflicting the effect to the caster when healing or buffing effects
- fix Tier 1 Relics Attribute Modifiers not working

# 1.0.0 - 1.21.1
- Initial Release!