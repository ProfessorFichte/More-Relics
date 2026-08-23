package more_relics.mixin;

import more_relics.spell.MoreRelicEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.spell_engine.internals.target.EntityRelations;
import net.spell_engine.internals.target.SpellTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityRelations.class, remap = false)
public class EntityRelationsMixin {
    @Inject(method = "actionAllowed", at = @At("HEAD"), cancellable = true)
    private static void moreRelics$blockUntargetable(SpellTarget.FocusMode mode, SpellTarget.Intent intent, LivingEntity caster, Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (target != caster && target instanceof LivingEntity livingTarget
                && (livingTarget.hasStatusEffect(MoreRelicEffects.SUPERIOR_ZHONYAS_HOURGLASS.entry) || livingTarget.hasStatusEffect(MoreRelicEffects.SUPERIOR_GUARDIAN_ANGEL.entry))) {
            cir.setReturnValue(false);
        }
    }
}
