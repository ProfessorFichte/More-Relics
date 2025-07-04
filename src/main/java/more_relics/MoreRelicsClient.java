package more_relics;

import more_relics.spell.MoreRelicEffects;
import more_relics.spell.MoreRelicSpells;
import net.fabricmc.api.ClientModInitializer;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;

import java.util.List;

public class MoreRelicsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		for (var entry: MoreRelicSpells.entries) {
			if (entry.mutator() != null) {
				SpellTooltip.addDescriptionMutator(entry.id(), entry.mutator());
			}
		}

		CustomParticleStatusEffect.register(
				MoreRelicEffects.LESSER_POWER_AIR_LIGHTNING.effect,
				new BuffParticleSpawner(List.of("more_rpg_classes:small_gust", "spell_engine:electric_arc_a"), 0.5F, 0.11F, 0.12F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.LESSER_POWER_EARTH_FIRE.effect,
				new BuffParticleSpawner(List.of("more_rpg_classes:stone_particle", "spell_engine:flame_spark"), 0.5F, 0.11F, 0.12F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.LESSER_POWER_WATER_FROST.effect,
				new BuffParticleSpawner(List.of("more_rpg_classes:big_splash", "spell_engine:snowflake"), 0.5F, 0.11F, 0.12F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.LESSER_RAGE_POWER.effect,
				new BuffParticleSpawner("spell_engine:magic_rage_stripe_float", 1.5F)
		);

		CustomParticleStatusEffect.register(
				MoreRelicEffects.MEDIUM_AIR_POWER.effect,
				new BuffParticleSpawner("more_rpg_classes:small_gust", 1.5F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.MEDIUM_EARTH_POWER.effect,
				new BuffParticleSpawner("more_rpg_classes:stone_particle", 1.5F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.MEDIUM_WATER_POWER.effect,
				new BuffParticleSpawner("more_rpg_classes:bubble", 1.5F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.MEDIUM_RAGE_POWER.effect,
				new BuffParticleSpawner("spell_engine:magic_rage_stripe_float", 1.5F)
		);

		CustomParticleStatusEffect.register(
				MoreRelicEffects.GREATER_RAGE_POWER.effect,
				new BuffParticleSpawner("spell_engine:magic_rage_stripe_float", 1.5F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.GREATER_FROZEN_HEART.effect,
				new BuffParticleSpawner("spell_engine:snowflake", 5.0F)
		);

		CustomParticleStatusEffect.register(
				MoreRelicEffects.GREATER_LIANDRYS_TORNMENT.effect,
				new BuffParticleSpawner("spell_engine:flame_medium_b", 0.5F)
						.withGroundEffect(
								SpellEngineParticles.area_effect_480.id().toString(),
								Color.RED,
								SpellEngineParticles.area_effect_480.texture().frames())
		);

		CustomParticleStatusEffect.register(
				MoreRelicEffects.SUPERIOR_ZHONYAS_HOURGLASS.effect,
				new BuffParticleSpawner("spell_engine:magic_holy_stripe_float", 4.0F)
						.withGroundEffect(
								SpellEngineParticles.ground_glow.id().toString(),
								Color.HOLY,
								SpellEngineParticles.ground_glow.texture().frames())
		);
	}
}