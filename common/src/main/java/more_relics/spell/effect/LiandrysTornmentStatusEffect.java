package more_relics.spell.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.LivingEntity;

public class LiandrysTornmentStatusEffect extends StatusEffect {
    public LiandrysTornmentStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    // 1.20.1 `applyUpdateEffect` returns void (the 1.21 `boolean` return, whose `false` ends the
    // effect early, does not exist). Nothing here relied on it.
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        float damage = entity.getMaxHealth() * 0.03F;
        entity.damage(entity.getDamageSources().magic(), damage);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        i = 20;
        if (i > 0) {
            return duration % i == 0;
        }
        return false;
    }
}
