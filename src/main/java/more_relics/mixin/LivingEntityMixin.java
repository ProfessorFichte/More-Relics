package more_relics.mixin;

import more_relics.spell.MoreRelicEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)

    private void zhonyas_damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Entity attacker = source.getAttacker();
        if (source.isIn(DamageTypeTags.BYPASSES_RESISTANCE) || source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)
                || attacker == null || amount <= 0
                || entity.getWorld().isClient()) {
            return;
        }
        if (entity.hasStatusEffect(MoreRelicEffects.SUPERIOR_ZHONYAS_HOURGLASS.entry)) {
            cir.cancel();
        }
    }
}
