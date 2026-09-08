package more_relics.spell.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import static net.more_rpg_classes.util.CustomMethods.clearNegativeEffects;

public class MikaelsBlessingStatusEffect extends StatusEffect {
    public MikaelsBlessingStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    // 1.20.1 signature carries the entity's `AttributeContainer`.
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        clearNegativeEffects(entity, false);
    }
}
