package more_relics.client.effect;

import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;

public class GuardianAngelParticleSpawner implements CustomParticleStatusEffect.Spawner {

    public static final ParticleGroup particles =
            ParticleGroupBuilder.magic(SpellEngineParticles.magic_stripe, ParticleGroup.Motion.ASCEND)
                    .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                            .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                            .count(10).speed(0.01F, 0.05F));

    @Override
    public void spawnParticles(LivingEntity livingEntity, int amplifier) {
        var world = livingEntity.getWorld();
        if (world.isClient) {
            ParticleHelper.play(world, livingEntity, particles);
        }
    }
}
