package more_relics.spell.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import static net.more_rpg_classes.util.CustomMethods.clearNegativeEffects;

public class MikaelsBlessingStatusEffect extends StatusEffect {
    public MikaelsBlessingStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        clearNegativeEffects(entity,false);
    }
}
