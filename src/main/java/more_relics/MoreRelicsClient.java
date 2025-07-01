package more_relics;

import more_relics.spell.MoreRelicEffects;
import more_relics.spell.MoreRelicSpells;
import net.fabricmc.api.ClientModInitializer;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.client.gui.SpellTooltip;

public class MoreRelicsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		for (var entry: MoreRelicSpells.entries) {
			if (entry.mutator() != null) {
				SpellTooltip.addDescriptionMutator(entry.id(), entry.mutator());
			}
		}
		CustomParticleStatusEffect.register(
				MoreRelicEffects.MEDIUM_AIR_POWER.effect,
				new BuffParticleSpawner("more_rpg_classes:small_gust", 0.5F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.MEDIUM_EARTH_POWER.effect,
				new BuffParticleSpawner("more_rpg_classes:stone_particle", 0.5F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.MEDIUM_WATER_POWER.effect,
				new BuffParticleSpawner("more_rpg_classes:bubble", 0.5F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.MEDIUM_RAGE_POWER.effect,
				new BuffParticleSpawner("spell_engine:magic_rage_stripe_float", 0.5F)
		);
	}
}