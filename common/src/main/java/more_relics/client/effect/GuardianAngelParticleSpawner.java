package more_relics.client.effect;

import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;

public class GuardianAngelParticleSpawner implements CustomParticleStatusEffect.Spawner {

    public static final ParticleBatch particles = new ParticleBatch(
            SpellEngineParticles.MagicParticles.get(
                    SpellEngineParticles.MagicParticles.Shape.STRIPE,
                    SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
            ParticleBatch.Shape.WIDE_PIPE,
            ParticleBatch.Origin.FEET,
            null,
            10,
            0.01F,
            0.05F,
            0);

    @Override
    public void spawnParticles(LivingEntity livingEntity, int amplifier) {
        var world = livingEntity.getWorld();
        if (world.isClient) {
            var scaledParticles = new ParticleBatch(particles);
            ParticleHelper.play(world, livingEntity, scaledParticles);
        }
    }
}