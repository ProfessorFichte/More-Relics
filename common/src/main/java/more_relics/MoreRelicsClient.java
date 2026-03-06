package more_relics;

import more_relics.client.effect.GuardianAngelParticleSpawner;
import more_relics.client.render.GoldenPlayerRenderLayer;
import more_relics.spell.MoreRelicEffects;
import more_relics.spell.MoreRelicSpells;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;

import java.util.List;

public class MoreRelicsClient {

	public static void init() {
		for (var entry: MoreRelicSpells.entries) {
			if (entry.mutator() != null) {
				SpellTooltip.addDescriptionMutator(entry.id(), entry.mutator());
			}
		}
		final Color ORANGE = new Color(255.0F, 165.0F, 0.0F);
		final Color PURPLE = new Color(104.0F, 12.0F, 104.0F);
		final Color GOLD = Color.from(0xffd700);

		var stripe_float = SpellEngineParticles.MagicParticles.get(
				SpellEngineParticles.MagicParticles.Shape.STRIPE,
				SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString();
		var arcane_ascend = SpellEngineParticles.MagicParticles.get(
				SpellEngineParticles.MagicParticles.Shape.ARCANE,
				SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString();

		CustomParticleStatusEffect.register(
				MoreRelicEffects.LESSER_POWER_AIR_WATER.effect,
				new BuffParticleSpawner(List.of("more_rpg_classes:small_gust", "more_rpg_classes:bubble"), 0.5F, 0.11F, 0.12F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.LESSER_POWER_EARTH_NATURE.effect,
				new BuffParticleSpawner(List.of("more_rpg_classes:stone_particle", "more_rpg_classes:leaf"), 0.25F, 0.11F, 0.12F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.LESSER_RAGE_POWER.effect,
				new BuffParticleSpawner(
						BuffParticleSpawner.defaultBatch(
								stripe_float,
								1,
								Color.RAGE.toRGBA())));

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
				MoreRelicEffects.MEDIUM_NATURE_POWER.effect,
				new BuffParticleSpawner("more_rpg_classes:leaf", 0.5F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.MEDIUM_RAGE_POWER.effect,
				new BuffParticleSpawner(
						BuffParticleSpawner.defaultBatch(
								stripe_float,
								1.5F,
								Color.RAGE.toRGBA())));

		CustomParticleStatusEffect.register(
				MoreRelicEffects.GREATER_RAGE_POWER.effect,
				new BuffParticleSpawner(
						BuffParticleSpawner.defaultBatch(
								stripe_float,
								1.5F,
								Color.RAGE.toRGBA())));

		CustomParticleStatusEffect.register(
				MoreRelicEffects.GREATER_FROZEN_HEART.effect,
				new BuffParticleSpawner(SpellEngineParticles.snowflake.id().toString(), 5.0F)
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.GREATER_LIANDRYS_TORMENT.effect,
				new BuffParticleSpawner(SpellEngineParticles.flame_medium_b.id().toString(), 0.5F)
						.withGroundEffect(
								SpellEngineParticles.area_effect_480.id().toString(),
								Color.RAGE,
								SpellEngineParticles.area_effect_480.texture().frames())
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.GREATER_MADREDS_BLOODRAZOR.effect,
				new BuffParticleSpawner(
						BuffParticleSpawner.defaultBatch(
								stripe_float,
								5.0F,
								Color.RED.toRGBA()))
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.SUPERIOR_MEJAIS_SOULSTEALER.effect,
				new BuffParticleSpawner(
						BuffParticleSpawner.defaultBatch(
								arcane_ascend,
								5.0F,
								Color.ARCANE.toRGBA()))
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.GREATER_SUNFIRE_CAPE.effect,
				new BuffParticleSpawner(SpellEngineParticles.flame_medium_b.id().toString(), 0.5F)
						.withGroundEffect(
								SpellEngineParticles.area_effect_293.id().toString(),
								ORANGE,
								SpellEngineParticles.area_effect_293.texture().frames())
		);

		CustomParticleStatusEffect.register(
				MoreRelicEffects.SUPERIOR_ZHONYAS_HOURGLASS.effect,
				new BuffParticleSpawner(
						BuffParticleSpawner.defaultBatch(
								stripe_float,
								4.0F,
								GOLD.toRGBA()))
						.withGroundEffect(
								SpellEngineParticles.ground_glow.id().toString(),
                                Color.fromRGBA(GOLD.toRGBA()),
								SpellEngineParticles.ground_glow.texture().frames())
		);
		CustomParticleStatusEffect.register(
				MoreRelicEffects.SUPERIOR_SHURELYAS_BATTLESONG.effect,
				new BuffParticleSpawner(
						BuffParticleSpawner.defaultBatch(
								stripe_float,
								4.0F,
								Color.HOLY.toRGBA()))
						.withGroundEffect(
								SpellEngineParticles.ground_glow.id().toString(),
								Color.WHITE,
								SpellEngineParticles.ground_glow.texture().frames())
		);
		CustomParticleStatusEffect.register(MoreRelicEffects.SUPERIOR_GUARDIAN_ANGEL.effect, new GuardianAngelParticleSpawner());

		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
			if (entityRenderer instanceof PlayerEntityRenderer playerRenderer) {
				registrationHelper.register(new GoldenPlayerRenderLayer(playerRenderer));
			}
		});
	}
}